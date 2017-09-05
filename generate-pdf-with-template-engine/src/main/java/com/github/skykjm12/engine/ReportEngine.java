package com.github.skykjm12.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.lowagie.text.DocumentException;

public class ReportEngine {
	private Handlebars handlebars;

	public ReportEngine() {
		// init the Handlebars instance
		handlebars = new Handlebars();
		// register our custom helpers, we'll get to this soon
		handlebars.registerHelpers(new HandlebarsHelpers());
	}

	public void generate(String templateFile, String outputFile, Map<String, Object> data) {
		try (OutputStream output = new FileOutputStream(new File(outputFile))) {
			// load the template (.hbs file) from classpath or an external file
			Template template = handlebars.compile(templateFile);

			// run Handlebars render with the input data
			String mergedTemplate = template.apply(data);
			// create a structured document from the generated HTML
			Document doc = getDocumentBuilder().parse(new ByteArrayInputStream(mergedTemplate.getBytes("UTF-8")));

			// now we take the document and apply the magic of flying saucer to
			// create a PDF file
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(doc, null);
			renderer.layout();
			renderer.createPDF(output);
			renderer.finishPDF();
		} catch (IOException | ParserConfigurationException | SAXException | DocumentException e) {
			e.printStackTrace();
		}
	}

	// we use this method to create a DocumentBuild not so fanatic about XHTML
	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		fac.setNamespaceAware(false);
		fac.setValidating(false);
		fac.setFeature("http://xml.org/sax/features/namespaces", false);
		fac.setFeature("http://xml.org/sax/features/validation", false);
		fac.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		fac.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		return fac.newDocumentBuilder();
	}
}
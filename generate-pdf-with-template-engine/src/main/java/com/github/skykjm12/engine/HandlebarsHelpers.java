package com.github.skykjm12.engine;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.jknack.handlebars.Options;
import com.github.skykjm12.domain.Customer;
import com.github.skykjm12.domain.Purchase;

public class HandlebarsHelpers {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	public CharSequence formatDateTime(final LocalDateTime date, final Options options) throws IOException {
		return formatter.format(date);
	}

	public CharSequence getYear(final LocalDate date, final Options options) throws IOException {
		return String.valueOf(date.getYear());
	}

	public CharSequence purchasesQty(final Customer c, final Options options) throws IOException {
		Integer qty = c.getPurchases().stream().mapToInt(Purchase::getQuantity).sum();

		return String.valueOf(qty);
	}

	public CharSequence purchasesTotal(final Customer c, final Options options) throws IOException {
		Double sum = c.getPurchases().stream().mapToDouble(Purchase::getValue).sum();

		return NumberFormat.getCurrencyInstance().format(sum);
	}

	public CharSequence formatCurrency(final Double value, final Options options) throws IOException {
		if (value == null)
			return "";
		return NumberFormat.getCurrencyInstance().format(value);
	}
}
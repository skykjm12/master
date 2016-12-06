<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
</head>
<body>
	<h2>게시판 목록</h2>
	<table class="board_list">
		<colgroup>
			<col width="10%"/>
			<col width="*"/>
			<col width="15%"/>
			<col width="20%"/>
		</colgroup>
		<thead>
			<tr>
				<th scope="col">글번호</th>
				<th scope="col">제목</th>
				<th scope="col">조회수</th>
				<th scope="col">작성일</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${fn:length(list) > 0}">
					<c:forEach items="${list }" var="row">
						<tr>
							<td>${row.IDX }</td>
							<td class="title">
								<a href="#this" name="title">${row.TITLE }</a>
								<input type="hidden" id="IDX" value="${row.IDX }">
							</td>
							<td>${row.HIT_CNT }</td>
							<td>${row.CREA_DTM }</td>
						</tr>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<tr>
						<td colspan="4">조회된 결과가 없습니다.</td>
					</tr>
				</c:otherwise>
			</c:choose>
		</tbody>
	</table>
	<br/>
	<br/>
	<div id="paging" style="margin-left: 190px;"></div>
	<br/>
	
	<a href="#this" class="btn" id="write">글쓰기</a>
	
	<form id="commonForm" name="commonForm">
	</form>

	<script type="text/javascript">
		$(document).ready(function(){

			$("#write").on("click", function(e){ //글쓰기 버튼
				e.preventDefault();
				fn_openBoardWrite();
			});	
			
			$("a[name='title']").on("click", function(e){ //제목 
				e.preventDefault();
				fn_openBoardDetail($(this));
			});
			
			$("#paging").html(Paging('${page.totalCount}', 10, 10 ,'${pageNo}', "boardList"));
		});
		
		
		function fn_openBoardWrite(){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardWrite.do' />");
			comSubmit.submit();
		}
		
		function fn_openBoardDetail(obj){
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardDetail.do' />");
			comSubmit.addParam("IDX", obj.parent().find("#IDX").val());
			comSubmit.submit();
		}
		
		function fn_openBoardList(cPage){ 
			var comSubmit = new ComSubmit();
			comSubmit.setUrl("<c:url value='/sample/openBoardList.do' />");
			comSubmit.addParam("pageNo", cPage);
			comSubmit.addParam("countPerPage", 10);
			comSubmit.submit();
		}
		
		var goPaging_boardList = function(cPage){
			fn_openBoardList(cPage); // fn_openBoardList 함수를 다시 호출
		};	
		
		Paging = function(totalCnt, dataSize, pageSize, pageNo, token){
	           totalCnt = parseInt(totalCnt);	// 전체레코드수
	           dataSize = parseInt(dataSize);   // 페이지당 보여줄 데이타수
	           pageSize = parseInt(pageSize);   // 페이지 그룹 범위       1 2 3 5 6 7 8 9 10
	           pageNo = parseInt(pageNo);       // 현재페이지
	          
	           var  html = new Array();
	           if(totalCnt == 0){
	                      return "";
	           }
	          
	           // 페이지 카운트
	           var pageCnt = totalCnt % dataSize;         
	           if(pageCnt == 0){
	                      pageCnt = parseInt(totalCnt / dataSize);
	           }else{
	                      pageCnt = parseInt(totalCnt / dataSize) + 1;
	           }
	          
	           var pRCnt = parseInt(pageNo / pageSize);
	           if(pageNo % pageSize == 0){
	                      pRCnt = parseInt(pageNo / pageSize) - 1;
	           }
	          
	           //이전 화살표
	           if(pageNo > pageSize){
	                      var s2;
	                      if(pageNo % pageSize == 0){
	                                  s2 = pageNo - pageSize;
	                      }else{
	                                  s2 = pageNo - pageNo % pageSize;
	                      }
	                      html.push('<a href=javascript:goPaging_' + token + '("');
	                      html.push(s2);
	                      html.push('");>');
	                      html.push('◀ ');
	                      html.push("</a>");
	           }else{
	                      html.push('<a href="#">\n');
	                      html.push('◀ ');
	                      html.push('</a>');
	           }
	          
	           //paging Bar
	           for(var index=pRCnt * pageSize + 1;index<(pRCnt + 1)*pageSize + 1;index++){
	                      if(index == pageNo){
	                                  html.push('<strong>');
	                                  html.push(index);
	                                  html.push('</strong>');
	                      }else{
	                                  html.push('<a href=javascript:goPaging_' + token + '("');
	                                  html.push(index);
	                                  html.push('");>');
	                                  html.push(index);
	                                  html.push('</a>');
	                      }
	                      if(index == pageCnt){
	                                  break;
	                      }else html.push('  |  ');
	           }
	            
	           //다음 화살표
	           if(pageCnt > (pRCnt + 1) * pageSize){
	                      html.push('<a href=javascript:goPaging_' + token + '("');
	                      html.push((pRCnt + 1)*pageSize+1);
	                      html.push('");>');
	                      html.push(' ▶');
	                      html.push('</a>');
	           }else{
	                      html.push('<a href="#">');
	                      html.push(' ▶');
	                      html.push('</a>');
	           }
	 
	           return html.join("");
	}

	</script>	
</body>
</html>

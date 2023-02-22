<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
//**********************************************************************************
// ○ 파일	: page_error.jsp
// ● 설명	: 페이지 공통 error
//**********************************************************************************
%>
<jsp:directive.page import="acms.common.GC, acms.common.GF"/>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>에러 페이지</title>
	</head>
	<body>
		<%
		String 에러코드 = GF.getErrorCode(request);
		if (에러코드.equals(GC.RESULT_NOT_AUTH)) {
			%>
			401 에러 페이지!!!
			<%
		} else if (에러코드.equals(GC.RESULT_NOT_EXIST)) {
			%>
			404 에러 페이지!!!
			<%
		} else if (에러코드.equals(GC.RESULT_ERROR)) {
			%>
			500 에러 페이지!!!
			<%
		} else {
			%>
			기타 에러 페이지!!!
			<%
		}
		%>
		<br/>에러코드 : <%= 에러코드 %>
	</body>
</html>
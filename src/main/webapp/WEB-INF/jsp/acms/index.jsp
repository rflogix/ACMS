<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
/***********************************************************************************
* ○ 파일	: index.jsp
* ● 설명	: 인덱스 화면
***********************************************************************************/
%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:directive.include file="common/index_head.jsp"/>
		<script src="acms/js/index.js<%= 리소스버젼 %>"></script>
		<link href="acms/css/index.css<%= 리소스버젼 %>" type="text/css" rel="stylesheet"/>
	</head>
	<body>
		<jsp:directive.include file="common/index_script.jsp"/>
	</body>
</html>
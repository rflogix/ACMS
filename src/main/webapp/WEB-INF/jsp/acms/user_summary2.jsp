<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
/***********************************************************************************
* ○ 파일	: user_summary.jsp
* ● 설명	: 사용자 관리 화면
***********************************************************************************/
%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:directive.include file="common/page_head.jsp"/>
		<script src="acms/js/user_summary.js<%= 리소스버젼 %>"></script>
	</head>
	<body>
		<jsp:directive.include file="common/page_header.jsp"/>
		
		<div class="page_content"></div>
		
		<jsp:directive.include file="common/page_footer.jsp"/>
	</body>
</html>
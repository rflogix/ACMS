<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
String test = "TEST";
%>
<!DOCTYPE html>
<html>
	<head>
		<title><%= test %></title>
		<script src="common/js/jquery/jquery-3.4.1.min.js"></script>
		<script src="test/js/common.js"></script>
	</head>
	<body>
		<%= test %> 사이트
		<br/><br/>
		
		JS 테스트 :
		<br/><div id="test"></div>
		<br/><br/>
	</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
//**********************************************************************************
// ○ 파일	: index_script.jsp
// ● 설명	: index script
//**********************************************************************************
%>
<script>
	GLOBAL.LOGIN_USER = ${LOGIN_USER}; // 로그인 사용자 객체 - RequestConfig.java 에서 세팅
	GLOBAL.CONTEXT_PATH = `${CONTEXT_PATH}`; // context path - RequestConfig.java 에서 세팅
</script>
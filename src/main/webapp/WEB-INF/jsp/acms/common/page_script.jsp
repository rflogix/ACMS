<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
//**********************************************************************************
// ○ 파일	: page_script.jsp
// ● 설명	: 페이지 공통 script
//**********************************************************************************
%>
<script>
	GLOBAL.LOGIN_USER = ${LOGIN_USER}; // 로그인 사용자 객체 - RequestConfig.java 에서 세팅
	GLOBAL.MENU = ${MENU}; // 선택된 메뉴 객체 - CommonService.java 에서 세팅
	GLOBAL.CONTEXT_PATH = `${CONTEXT_PATH}`; // context path - RequestConfig.java 에서 세팅
</script>
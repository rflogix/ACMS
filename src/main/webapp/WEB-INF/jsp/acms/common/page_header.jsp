<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
//**********************************************************************************
// ○ 파일	: page_header.jsp
// ● 설명	: 페이지 공통 header
//**********************************************************************************
%>
<!-- 헤더 -->
<div class="page_header flex">
	<div class="header_wrap flex-between">
		<div class="logo_wrap flex-left" onclick="location.href = GLOBAL.CONTEXT_PATH;">
			<img src="acms/image/common/header_logo.png"/>
			<span>ACMS</span>
		</div>
		<div class="btn_wrap flex-left">
			<button onclick="popupUserPw()">비밀번호 변경</button>
			<button onclick="logout()">로그아웃</button>
		</div>
	</div>
</div>
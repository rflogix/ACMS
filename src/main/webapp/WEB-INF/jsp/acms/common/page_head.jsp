<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
//**********************************************************************************
// ○ 파일	: page_head.jsp
// ● 설명	: 페이지 공통 head
//**********************************************************************************

String 리소스버젼 = "?v=20230102.1"; // CSS 나 JS 수정 후 업데이트 해줄것
%>
<title>ACMS</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link href="acms/image/favicon.ico" rel="icon" type="image/x-icon">

<!-- 폰트 -->
<jsp:directive.include file="page_font.jsp"/>

<!-- 상수 : 최상위에 설정 -->
<script src="acms/js/constant.js<%= 리소스버젼 %>"></script>
<link href="acms/css/constant.css<%= 리소스버젼 %>" type="text/css" rel="stylesheet"/>

<!-- jquery -->
<script src="common/js/jquery/jquery-3.4.1.min.js"></script>
<script src="common/js/jquery/jquery-ui-1.12.1.min.js"></script>
<link href="common/js/jquery/jquery-ui-1.12.1.min.css" type="text/css" rel="stylesheet"/>

<!-- dhtmlx  -->
<script src="common/js/dhtmlx/suite-7.3.14/codebase/suite.js"></script>
<link href="common/js/dhtmlx/suite-7.3.14/codebase/suite.css" type="text/css" rel="stylesheet"/>

<!-- 공통 -->
<script src="common/js/base.js<%= 리소스버젼 %>"></script>
<link href="common/css/base.css<%= 리소스버젼 %>" type="text/css" rel="stylesheet"/>

<!-- 도메인별 참조 -->
<script src="acms/js/common.js<%= 리소스버젼 %>"></script>
<link href="acms/css/common.css<%= 리소스버젼 %>" type="text/css" rel="stylesheet"/>
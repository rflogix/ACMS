<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%
/***********************************************************************************
* ○ 파일	: login.jsp
* ● 설명	: 로그인 화면
***********************************************************************************/
%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:directive.include file="common/page_head.jsp"/>
		
		<script src="acms/js/login.js<%= 리소스버젼 %>"></script>
		<link href="acms/css/login.css<%= 리소스버젼 %>" type="text/css" rel="stylesheet"/>
		
		<script>
			// 쿠키에서 userId 설정
			cookieUserId = `${COOKIE_USER_ID}`;
		</script>
	</head>
	<body class="flex">
		<div class="login_wrap flex">
			<div class="left_wrap">
				<img src="acms/image/login/login_bg.jpg"/>
			</div>
			<div class="right_wrap">
				<div class="logo_wrap flex-col">
					<img src="acms/image/login/login_logo.jpg"/>
					<label>ACMS</label>
				</div>
				<div class="login_label flex">
					<label>
						로그인이 필요한 서비스입니다.
					</label>
				</div>
				<form class="flex-col">
					<div class="flex-col-left">
						<label>User ID</label>
						<input name="userId" placeholder="아이디를 입력하세요" autocomplete="off"/>
					</div>
					<div class="user_pw flex-col-left">
						<label>Password</label>
						<input name="userPw" placeholder="비밀번호를 입력하세요" type="password" autocomplete="off"/>
					</div>
				</form>
				<div class="remember_wrap flex-left">
					<input id="saveIdYn" type="checkbox"/>
					<label for="saveIdYn">
						Remember me
					</label>
				</div>
				<button class="btn_login flex" onclick="loginUser()" type="button">
					Login
				</button>
			</div>
		</div>
	</body>
</html>
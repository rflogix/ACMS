//**********************************************************************************
// ○ 파일	: login.js
// ● 설명	: 로그인 js
//**********************************************************************************

// 화면 처리
let cookieUserId = '';
$(document).ready(function() {
	// 엔터 처리
	$('[name=userId]').keyup(function(e) {
		if (e.keyCode == 13) {
			$('[name=userPw]').focus();
		}
	});
	$('[name=userPw]').keyup(function(e) {
		if (e.keyCode == 13) {
			loginUser();
		}
	});
	
	// 아이디 저장 설정
	if (cookieUserId != '') {
		// 로그인 사용자 아이디 설정
		$('[name=userId]').val(cookieUserId);
		$('#saveIdYn').prop('checked', true);
		$('[name=userPw]').focus();
		
	} else {
		$('[name=userId]').focus();
	}
});

// 로그인
function loginUser() {
	if (SEARCHING_YN == false) {
		let selectorId = '[name=userId]';
		$(selectorId).val($.trim($(selectorId).val()));
		if ($(selectorId).val() == '') { // 아이디 입력 안됐을때
			alert('ID를 입력해 주세요.', function() {
				$(selectorId).focus();
			});
			return false;
		}

		selectorId = '[name=userPw]';
		$(selectorId).val($.trim($(selectorId).val())).val();
		if ($(selectorId).val() == '') { // 비번 입력 안됐을때
			alert('비밀번호를 입력해 주세요.', function() {
				$(selectorId).focus();
			});
			return false;
		}
		
		SEARCHING_YN = true;
		
		let param = $('.login_wrap form').toJson();
		if ($('#saveIdYn').prop('checked')) {
			param.saveIdYn = 'Y';
		}
		$.post('user/login', param, function(data) {
			if (data.result == RESULT_OK) { // 로그인 했다면
				location.replace(GLOBAL.CONTEXT_PATH);
				
			} else if ((data.result == RESULT_EMPTY_ID) // ID가 비어있거나
				|| (data.result == RESULT_NOT_EXIST_ID) // ID 가 존재하지 않거나
			) {
				alert(data.message, function() {
					$('[name=userId]').select().focus();
				});
				
			} else if ((data.result == RESULT_EMPTY_PW) // PW가 비어있거나
				|| (data.result == RESULT_NOT_MATCH_PW) // PW 가 일치하지 않거나
				|| (data.result == RESULT_OVER_COUNT_PW) // 회수를 넘겼거나
			) {
				alert(data.message, function() {
					$('[name=userPw]').select().focus();
				});
			
			} else if (data.result == RESULT_OVER_PERIOD_PW) { // 비밀번호 일치 후 비밀번호 만료일이 경과했다면
				alert(data.message, function() {
					popupUserPw(data.userId); // 비밀번호 변경 팝업창
				});
				
			} else if (data.result == RESULT_NEED_NEW_PW) { // 비밀번호 일치 후 비밀번호를 변경한적이 없다면
				alert(data.message, function() {
					popupUserPw(data.userId); // 비밀번호 변경 팝업창
				});
				
			} else {
				alert(data.message);
			}
			SEARCHING_YN = false;
			
		}, 'JSON').fail(function() {
			alert('네트워크 문제로 로그인 되지 않았습니다');
			SEARCHING_YN = false;
		});
	}
}
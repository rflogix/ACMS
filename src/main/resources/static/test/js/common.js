//**********************************************************************************
// Copyright (C)RFLOGIX since 2010.08.24 (rflogix@rflogix.com)
//**********************************************************************************
// ○ 파일	: common.js
// ● 설명	: 공통 js
//**********************************************************************************

$(document).ready(function () {
	setTimeout(function() {
		$('#test').html('자바 스크립트 동작 테스트 시작!!!');
	}, 100);
	setTimeout(function() {
		$('#test').html('<span style="color: red;">자바 스크립트 동작 테스트 완료</span>');
	}, 1000);
});
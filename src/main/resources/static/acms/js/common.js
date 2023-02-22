//**********************************************************************************
// 비밀번호
//**********************************************************************************

// 비밀번호 양식 체크
function chkPassword(element) {
	let successYn = false;
	
	let password = $(element).val();
	let num = password.search(/[0-9]/g);
	let eng = password.search(/[a-z]/ig);
	let spe = password.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi);
	if (password.length < 8) {
		alert("비밀번호는 8자리 이상이어야 합니다", function() {
			$(element).select().focus();
		});

	} else if (password.search(/\s/) != -1){
		alert("비밀번호에는 공백을 입력할 수 없습니다.", function() {
			$(element).select().focus();
		});

	} else if ( (num < 0 && eng < 0) || (eng < 0 && spe < 0) || (spe < 0 && num < 0) ){
		alert("영문, 숫자, 특수문자 중 2가지 이상을 혼합하여야 합니다.", function() {
			$(element).select().focus();
		});
	
	} else {
		successYn = true;
	}
	
	if (successYn == true) {
		let SamePass_0 = 0; // 동일문자 카운트
		let SamePass_1 = 0; // 연속성(+) 카운트
		let SamePass_2 = 0; // 연속성(-) 카운트
		for (let i=0; i < password.length; i++) {
			if (i >= 2) {
				let chr_pass_0 = password.charCodeAt(i-2);
				let chr_pass_1 = password.charCodeAt(i-1);
				let chr_pass_2 = password.charCodeAt(i);
				
				// 동일문자 카운트
				if ((chr_pass_0 == chr_pass_1) && (chr_pass_1 == chr_pass_2)) {
					SamePass_0++;
				}
				
				// 연속성(+) 카운트
				if (chr_pass_0 - chr_pass_1 == 1 && chr_pass_1 - chr_pass_2 == 1) {
					SamePass_1++;
				}
				
				// 연속성(-) 카운트
				if (chr_pass_0 - chr_pass_1 == -1 && chr_pass_1 - chr_pass_2 == -1) {
					SamePass_2++;
				}
			}
		}
		// 동일문자 3자 이상 연속 입력 금지
		if (SamePass_0 > 0) {
			successYn = false;
			alert("동일문자를 3자 이상 연속 입력할 수 없습니다.", function() {
				$(element).select().focus();
			});
			
		// 영문, 숫자는 3자 이상 연속 입력금지
		} else if (SamePass_1 > 0 || SamePass_2 > 0 ) {
			successYn = false;
			alert("영문, 숫자는 3자 이상 연속 입력할 수 없습니다.", function() {
				$(element).select().focus();
			});
		}
	}
	return successYn;
}




//**********************************************************************************
// 콤보박스
//**********************************************************************************

// 메뉴 콤보박스 설정
function setAuthSelectBox(uiElement, uiProperty) { // select 요소, ui 파라미터
	let param = { // 조회 기본값
		useYn: 'Y'
		,deleteYn: 'N'
	};
	if (uiProperty.hasOwnProperty('setSelectBoxParam') == true) {
		param = mergeJson(param, uiProperty.setSelectBoxParam); // 추가 검색 조건 merge
	}
	post({
		url: 'menu/search'
		,data: param
		,success: function(data) {
			if (data.result == RESULT_OK) {
				let param = {
					list: data.list
					,value: 'authSeq'
					,text: 'authNm'
					
					,uiElement: uiElement
					,uiProperty: uiProperty
				};
				setSelectBox(param);
			}
		}
	});
}
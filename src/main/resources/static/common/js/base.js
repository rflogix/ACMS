//**********************************************************************************
// 요소 설정 관련
//**********************************************************************************

// 파라미터 요소를 필수로 설정하는 함수
function setRequire(element, requireYn) {
	if ($(element).length > 0) {
		if (requireYn == undefined) {
			requireYn = true;
		}
		if (requireYn == true) {
			// 상위 wrap 요소에 require 클래스명 설정
			if ($(element).parent().hasClass(LAYOUT_PARAM_WRAP) == true) {
				$(element).parent().addClass('require');
			}
			
			// 현재 요소에 클래스명 및 attr 세팅
			$(element).addClass('require').attr('require', requireYn);
			
		} else {
			// 상위 wrap 요소에 require 클래스명 설정
			if ($(element).parent().hasClass(LAYOUT_PARAM_WRAP) == true) {
				$(element).parent().removeClass('require');
			}
			
			// 현재 요소에 클래스명 및 attr 세팅
			$(element).removeClass('require').removeAttr('require');
		}
	}
}

// 파라미터 요소를 읽기 전용으로 설정하는 함수
function setReadonly(element, readonlyYn) {
	if ($(element).length > 0) {
		let tagName = $(element).prop('tagName').toLowerCase();
		
		if (readonlyYn == undefined) {
			readonlyYn = true;
		}
		if (readonlyYn == true) {
			// 상위 wrap 요소에 readonly 클래스명 설정
			if ($(element).parent().hasClass(LAYOUT_PARAM_WRAP) == true) {
				$(element).parent().addClass('readonly');
			}
			
			// 현재 요소에 클래스명 및 attr 세팅
			$(element).addClass('readonly').attr('readonly', readonlyYn);
			
		} else {
			// 상위 wrap 요소에 readonly 클래스명 설정
			if ($(element).parent().hasClass(LAYOUT_PARAM_WRAP) == true) {
				$(element).parent().removeClass('readonly');
			}
			
			// 현재 요소에 클래스명 및 attr 세팅
			$(element).removeClass('readonly').removeAttr('readonly');
		}
		
		// select box 일때 처리
		if (tagName == 'select') {
			setSelectboxReadonly(element, readonlyYn);
		}
	}
}

// readonly selectbox 설정
function setSelectboxReadonly(element, readonlyYn) {
	if (readonlyYn == undefined) {
		readonlyYn = true;
	}
	if (readonlyYn == true) {
		$(element).addClass('readonly').attr('readonly', readonlyYn).find('option').attr('disabled', 'disabled');
		$(element).find('option:selected').removeAttr('disabled');
		
	} else {
		$(element).removeClass('readonly').removeAttr('readonly').find('option').removeAttr('disabled');
	}
}

// 파라미터 요소를 에러로 설정하는 함수
function setError(element, errorYn) {
	if ($(element).length > 0) {
		if (errorYn == undefined) {
			errorYn = true;
		}
		
		// 상위 wrap 요소에 require 클래스명 설정
		if ($(element).parent().hasClass(LAYOUT_PARAM_WRAP) == true) {
			if (errorYn == true) {
				$(element).parent().addClass('error');
				
			} else {
				$(element).parent().removeClass('error');
			}
		}
		
		// 현재 요소에 클래스명 및 attr 세팅
		if (errorYn == true) {
			$(element).addClass('error').attr('error', errorYn);
			
		} else {
			$(element).removeClass('error').removeAttr('error');
		}
	}
}

// name 으로 Property 가져오기
function getPropertyByName(elements, name) { // elements 는 property 배열
	let findProperty = undefined;
	for (let property of elements) {
		if (property.hasOwnProperty('name') == true) {
			if (property.name = name) {
				findProperty = property;
				break;
			}
		}
	}
	
	return findProperty;
}



//**********************************************************************************
// 콤보박스
//**********************************************************************************

// 콤보박스 옵션 세팅
function setSelectBox(param) {
	// param = { // param 으로 넘어와야할 형태는 아래와 같다
	//	list: data.list, // 옵션에 뿌려질 리스트
	//	value: 'authrtId', // value 값으로 쓰일 요소
	//	text: 'authrtNm', // text 값으로 쓰일 요소
	//	uiElement: uiElement, // select 요소
	//	uiProperty: uiProperty, // select 요소에 추가될 항목이나 readonly 같은 옵션값들
	//}
	
	let uiElement = param.uiElement;
	let uiProperty = param.uiProperty;
	let selectValue = $(uiElement).val();
	
	$(uiElement).empty(); // 옵션 초기화
	
	// 앞에 붙일 옵션 추가 (ex. 전체)
	if (uiProperty.hasOwnProperty('prependSelectBox') == true) {
		if ((uiProperty.prependSelectBox != undefined) && (uiProperty.prependSelectBox != null)) {
			$(uiElement).append($(uiProperty.prependSelectBox));
		}
	}
	
	// 리스트 세팅
	let setValueTextYn = false;
	if ((param.hasOwnProperty('value') == true) && (param.hasOwnProperty('text') == true)) {
		setValueTextYn = true;
	}
	for (let row of param.list) {
		// value, text 설정해야 한다면 속성 설정
		if (setValueTextYn == true) {
			row.value = row[param.value];
			row.text = row[param.text];
		}
		
		// 옵션 생성
		let optionElement = $('<option>').attr('value', row.value).text(row.text);
		if (row.hasOwnProperty('className') == true) {
			$(optionElement).addClass(row.className);
		}
		$(uiElement).append($(optionElement)); // 옵션 추가
	}
	
	// 뒤에 붙일 옵션 추가 (ex. 전체)
	if (uiProperty.hasOwnProperty('appendSelectBox') == true) {
		if ((uiProperty.appendSelectBox != undefined) && (uiProperty.appendSelectBox != null)) {
			$(uiElement).append($(uiProperty.appendSelectBox));
		}
	}
	
	// 읽기전용 여부
	if (uiProperty.hasOwnProperty('readonly') == true) {
		setReadonly(uiElement, uiProperty.readonly);
	}
	
	// 이전 선택값 다시 선택
	$(uiElement).val(selectValue);
}




//**********************************************************************************
// 포멧 관련
//**********************************************************************************

// 3자리마다 콤마(화폐표시)
function formatMoney(p_param) {
	let strFormat = p_param.toString();
	let minus = strFormat.substr(0,1);
	strFormat = strFormat.replace(/[^\.0-9]/g, ''); // 점과 숫자가 아닌것을 제거  /\D/g /[^-\.0-9]/g
	strFormat = strFormat.replace(/\B(?=(\d{3})+(?!\d))/g, ','); // 콤마	/(^[+-]?\d+)(\d{3})/g /\B(?=(\d{3})+(?!\d))/g
	if (minus == '-') {strFormat = '-'+strFormat;}
	return strFormat;
}

// 날짜
function formatDate(p_param) { 
	let strFormat = p_param.toString();
	strFormat = strFormat.replace(/[^0-9]/g,'');
	strFormat = strFormat.substr(0,8);
	if (strFormat.length <= 3) {
		strFormat = strFormat.replace(/([0-9]{2})([0-9]{1}$)/,'$1-$2');
	} else if (strFormat.length <= 4) {
		strFormat = strFormat.replace(/([0-9]{2})([0-9]{2}$)/,'$1-$2');
	} else if (strFormat.length <= 5) {
		strFormat = strFormat.replace(/([0-9]{4})([0-9]{1}$)/,'$1-$2');
	} else if (strFormat.length <= 6) {
		strFormat = strFormat.replace(/([0-9]{4})([0-9]{2}$)/,'$1-$2');
	} else if (strFormat.length <= 7) {
		strFormat = strFormat.replace(/([0-9]{4})([0-9]{2})([0-9]{1}$)/,'$1-$2-$3');
	} else {
		strFormat = strFormat.replace(/([0-9]{4})([0-9]{2})([0-9]{2}$)/,'$1-$2-$3');
	}
	return strFormat;
}

// 전화번호
function formatTel(p_param) { 
	let strFormat = p_param.toString();
	strFormat = strFormat.replace(/[^0-9]/g,'');
	strFormat = strFormat.substr(0,12);
	let strLeft = strFormat.substring(0,2); // 서울국번 기본체크
	let CellPhone = '010,011,012,015,016,017,018,019';
	let LocalPhone = '02,03,04,05,06';
	if (strFormat.length <= 3) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{1}$)/,'$1-$2');
		} else {
			strFormat = strFormat.replace(/([0-9]{3})/,'$1');
		}
	} else if (strFormat.length == 4) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{2}$)/,'$1-$2');
		} else {
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{1}$)/,'$1-$2');
		}
	} else if (strFormat.length == 5) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{3}$)/,'$1-$2');
		} else {
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{2}$)/,'$1-$2');
		}
	} else if (strFormat.length == 6) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{3})([0-9]{1}$)/,'$1-$2-$3');
		} else {
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{3}$)/,'$1-$2');
		}
	} else if (strFormat.length == 7) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{3})([0-9]{2}$)/,'$1-$2-$3');
		} else {
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{4}$)/,'$1-$2');
		}
	} else if (strFormat.length == 8) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{3})([0-9]{3}$)/,'$1-$2-$3');
		} else if (LocalPhone.indexOf(strLeft) > -1) { // 지방번호체크
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{4})([0-9]{1}$)/,'$1-$2-$3'); // 없는번호-중간단계
		} else {
			strLeft = strFormat.substring(0,3);
			if (CellPhone.indexOf(strLeft) > -1) { // 핸드폰 체크
				strFormat = strFormat.replace(/([0-9]{3})([0-9]{4})([0-9]{1}$)/,'$1-$2-$3'); // 없는번호-중간단계
			} else {
				strFormat = strFormat.replace(/([0-9]{4})([0-9]{4}$)/,'$1-$2'); // 가능번호 (국번없이 적었다면)
			}
		}
	} else if (strFormat.length == 9) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{3})([0-9]{4}$)/,'$1-$2-$3'); // 가능번호
		} else {
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{4})([0-9]{2}$)/,'$1-$2-$3'); // 없는번호-중간단계
		}
	} else if (strFormat.length == 10) {
		if (strLeft == '02') { // 서울체크
			strFormat = strFormat.replace(/([0-9]{2})([0-9]{4})([0-9]{4}$)/,'$1-$2-$3'); // 가능번호
		} else {
			strFormat = strFormat.replace(/([0-9]{3})([0-9]{3})([0-9]{4}$)/,'$1-$2-$3'); // 가능번호
		}
	} else if (strFormat.length == 11) {
		strFormat = strFormat.replace(/([0-9]{3})([0-9]{4})([0-9]{4}$)/,'$1-$2-$3');
	} else {
		strFormat = strFormat.replace(/([0-9]{4})([0-9]{4})([0-9]{4}$)/,'$1-$2-$3');
	}
	return strFormat;
}




//**********************************************************************************
// 날짜 관련
//**********************************************************************************

// 날짜 차이
function getDayDiff(start, end) {
	let dayDiff = -1;
	if ((start != undefined) && (start != null) && (end != undefined) && (end != null)) {
		start = new Date(start.left(10) + ' 00:00:00');
		end = new Date(end.left(10) + ' 00:00:00');
		dayDiff = (end.getTime() - start.getTime()) / (1000*60*60*24);
	}
	
	return dayDiff;
}




//**********************************************************************************
// 화면 요소 관련
//**********************************************************************************

// validation form
function validationForm(element) {
	let validateYn = true;
	if (element != undefined) {
		$(element).find('.error').removeClass('error');
		$(element).find('input,select').each(function(index, element) {
			if ($(element).hasClass('require') == true) {
				$(element).val($(element).val().trim());
				if ($(element).val() == '') {
					alert('필수값이 누락되었습니다', function() {
						setError(element);
						$(element).focus();
					});
					validateYn = false;
					return false;
				}
			}
		});
		$(element).find('textarea').each(function(index, element) {
			if ($(element).hasClass('require') == true) {
				$(element).text($(element).text().trim());
				if ($(element).text() == '') {
					alert('필수값이 누락되었습니다', function() {
						setError(element);
						$(element).focus();
					});
					validateYn = false;
					return false;
				}
			}
		});
	}
	
	return validateYn;
}

// 요소의 버젼 업데이트
function updateCheckVer(element, checkVer) {
	let versionUpdateYN = false;
	let userIdCheckVerAttr = 'check-ver';
	
	if ($(element).hasAttr(userIdCheckVerAttr) == false) {
		$(element).attr(userIdCheckVerAttr, 0);
	}
	let beforeUserIdCheckVer = Number($(element).attr(userIdCheckVerAttr));
	if (beforeUserIdCheckVer < Number(checkVer)) { // 버젼이 높을때
		$(element).attr(userIdCheckVerAttr, checkVer); // 버젼 업데이트
		versionUpdateYN = true;
	}
	
	return versionUpdateYN;
}




//**********************************************************************************
// 문자열 관련
//**********************************************************************************

// String 오버라이딩
String.prototype.left = function(length) {
	if (this.length <= length) {
		return this;
	} else {
		return this.substring(0, length);
	}
}
String.prototype.right = function(length) {
	if (this.length <= length) {
		return this;
	} else {
		return this.substring(this.length - length, this.length);
	}
}




//**********************************************************************************
// 체크 함수 
//**********************************************************************************

// 숫자형인지 체크
function isNumber(num, opt){
	let numberYn = false;
	
	// 좌우 trim(공백제거)을 해준다.
	num = String(num).replace(/^\s+|\s+$/g, '');
	
	let regex = /^[0-9]$/g; // only 숫자만(부호 미사용, 자릿수구분기호 미사용, 소수점 미사용)
	if (typeof opt == 'undefined' || opt == '1') { // 모든 10진수 (부호 선택, 자릿수구분기호 선택, 소수점 선택)
		regex = /^[+\-]?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+){1}(\.[0-9]+)?$/g;
	
	 }else if(opt == '2') { // 부호 미사용, 자릿수구분기호 선택, 소수점 선택
		regex = /^(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+){1}(\.[0-9]+)?$/g;
		
	} else if(opt == '3') { // 부호 미사용, 자릿수구분기호 미사용, 소수점 선택
		regex = /^[0-9]+(\.[0-9]+)?$/g;
	}
	if (regex.test(num)) {
		num = num.replace(/,/g, '');
		numberYn = !isNaN(num);
	}
	
	return numberYn;
}



//**********************************************************************************
// 기타 사용자 정의 
//**********************************************************************************

// post 전송
function post(param) {
	if (param.hasOwnProperty('url') == true) {
		if (param.hasOwnProperty('async') == false) { // 비동기여부
			param.async = true;
		}
		if (param.hasOwnProperty('type') == false) { // 전송방식
			param.type = 'post';
		}
		if (param.hasOwnProperty('dataType') == false) { // 리턴받는 데이터 타입
			param.dataType = 'json';
		}
		if (param.hasOwnProperty('contentType') == false) { // 전송 내용 타입
			param.contentType = 'application/x-www-form-urlencoded; charset=UTF-8';
		}
		if (param.hasOwnProperty('traditional') == false) { // 배열 직렬화
			param.traditional = true;
		}
		if (param.hasOwnProperty('data') == false) {
			param.data = {};
		}
		if (param.hasOwnProperty('success') == false) {
			param.success = function() {};
		}
		if (param.hasOwnProperty('error') == false) {
			param.error = function() {};
		}
		
		$.ajax({
			url: param.url,
			async: param.async,
			type: param.type,
			dataType: param.dataType,
			
			contentType: param.contentType,
			traditional: param.traditional,
			
			data: param.data,
			success: function(data) {
				param.success(data);
			},
			error: function(e) {
				console.error(e);
				param.error(e);
			},
		});
		
	} else {
		console.error('[common.js] post() url 값이 설정되지 않아 실행 불가');
	}
}

// post Multipart 전송
function postMultipart(param) {
	if (param.hasOwnProperty('url') == true) {
		if (param.hasOwnProperty('async') == false) { // 비동기여부
			param.async = true;
		}
		if (param.hasOwnProperty('type') == false) { // 전송방식
			param.type = 'post';
		}
		if (param.hasOwnProperty('dataType') == false) { // 리턴받는 데이터 타입
			param.dataType = 'json';
		}
		if (param.hasOwnProperty('enctype') == false) { // 전송 내용 타입
			param.enctype = 'multipart/form-data';
		}
		if (param.hasOwnProperty('processData') == false) {
			param.processData = false;
		}
		if (param.hasOwnProperty('contentType') == false) {
			param.contentType = false;
		}
		if (param.hasOwnProperty('cache') == false) {
			param.cache = false;
		}
		if (param.hasOwnProperty('data') == false) {
			param.data = {};
		}
		if (param.hasOwnProperty('success') == false) {
			param.success = function() {};
		}
		if (param.hasOwnProperty('error') == false) {
			param.error = function() {};
		}
		
		$.ajax({
			url: param.url,
			async: param.async,
			type: param.type,
			dataType: param.dataType,
			
			enctype: param.enctype,
			processData: param.processData,
			contentType: param.contentType,
			cache: param.cache,
			
			data: param.data,
			success: function(data) {
				param.success(data);
			},
			error: function(e) {
				console.error(e);
				param.error(e);
			},
		});
		
	} else {
		console.error('[common.js] post() url 값이 설정되지 않아 실행 불가');
	}
}

// json 병합
function mergeJson() {
	let param = {};
	for (let json of Array.prototype.slice.call(arguments)) {
		param = $.extend({}, param, json);
	}
	
	return param;
}

// 깊은 복사
function deepCopy(data) { // json 형태
	return $.extend(true, {}, data);
}
function deepCopyArray(data) { // array 형태
	return $.extend(true, [], data);
}

// Attr 존재하는지 체크
$.fn.hasAttr = function(name) {  
	return this.attr(name) !== undefined;
};

// 해당 요소로 스크롤
$.fn.scrollToMe = function (p_margin_top, p_parent, p_delay) {
	let element = $(this);
	let blnCheck = true;
	if (p_parent == undefined) { // 부모객체 안정해졌다면 부모들중 overflow-y:auto 인것을 부모로 설정
		if ($(element).parent().length > 0) {
			element = $(element).parent();
			if ($(element).css('overflow-y') == 'auto') {
				p_parent = $(element);
			} else {
				while ($(element).css('overflow-y') != 'auto') {
					if ($(element).parent().length > 0) {
						element = $(element).parent();
					} else {
						blnCheck = false;
						break;
					}
				}
				if (blnCheck) {
					p_parent = $(element);
				}
			}
		} else {
			blnCheck = false;
		}
	}
	if (p_margin_top == undefined) {
		p_margin_top = -($(p_parent).height() / 2);
	} else {
		p_margin_top = -1 * p_margin_top;
	}
	if (p_delay == undefined) {
		p_delay = 300;
	}
	if (blnCheck) {
		element = $(this);
		let position_y = $(element).position().top + $(p_parent).scrollTop();
		while ($(element).get(0) != $(p_parent).get(0)) {
			if ($(element).parent().length > 0) {
				position_y += $(element).position().top + Number($(element).css('margin-top').replace('px', ''));
				element = $(element).parent();
			} else {
				blnCheck = false;
				break;
			}
		}
		if (blnCheck) {
			position_y += p_margin_top;
			$(p_parent).stop().animate({scrollTop: position_y}, p_delay);
		}
	}
};

// form 요소들을 json 형태로 변경해 주는 함수
$.fn.toJson = function() {
	let obj = {};
	try {
		let form = this[0];
		if (form.tagName != undefined) {
			if (form.tagName.toLowerCase() == 'form') {
				for (let json of $(form).serializeArray()) {
					obj[json.name] = json.value;
				}
			}
		}
		
	} catch(e) {
		obj = {};
	}
 
	return obj;
};

// 요소의 높이 구하기
$.fn.getHeight = function() {
	let height = 0;
	let element = this[0];
	if (element != undefined) {
		let margin = Number($(element).css('margin-top').replace('px', '')) + Number($(element).css('margin-bottom').replace('px', ''));
		let padding = Number($(element).css('padding-top').replace('px', '')) + Number($(element).css('padding-bottom').replace('px', ''));
		height = $(element).height() + margin + padding;
	}
	
	return height;
}

// 요소의 마진 구하기
$.fn.getMargin = function() {
	let margin = 0;
	let element = this[0];
	if (element != undefined) {
		margin = Number($(element).css('margin-top').replace('px', '')) + Number($(element).css('margin-bottom').replace('px', ''));
	}
	
	return margin;
}

// 요소의 패딩 구하기
$.fn.getPadding = function() {
	let padding = 0;
	let element = this[0];
	if (element != undefined) {
		padding = Number($(element).css('padding-top').replace('px', '')) + Number($(element).css('padding-bottom').replace('px', ''));
	}
	
	return padding;
}
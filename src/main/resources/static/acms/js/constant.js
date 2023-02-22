//**********************************************************************************
// 공통
//**********************************************************************************

let GLOBAL = {CONTEXT_PATH: '/'}; // page_script.jsp 에서 설정
let UPDATING_YN = false; // 업데이트중 여부
let SEARCHING_YN = false; // 조회중 여부
let POPUP_SEARCHING_YN = false; // 팝업 조회중 여부




//**********************************************************************************
// GC.java 와 동일하게 설정할 값
//**********************************************************************************

let COOKIE_USER_ID = 'COOKIE_USER_ID';
let MENU_TYPE_HORIZON = 1;
let MENU_TYPE_VERTICAL = 2;




//**********************************************************************************
// 결과값 - GC.java 와 동일하게 설정할것
//**********************************************************************************

let SEARCH_PARAM = 'searchParam';
let RESULT_CODE = 'result';
let RESULT_MESSAGE = 'message';
let RESULT_LIST = 'list';

let RESULT_OK = 'OK'; // 성공
let RESULT_SUCCESS = RESULT_OK;
let RESULT_FAIL = 'FAIL'; // 실패
let RESULT_ERROR = 'ERROR'; // 에러
let RESULT_DUPLICATE = 'DUPLICATE'; // 중복
let RESULT_NOT_NULL = 'NOT_NULL'; // NULL 허용 안함
let RESULT_NOT_EXIST = 'NOT_EXIST'; // 존재하지 않음
let RESULT_NOT_EXIST_ID = 'NOT_EXIST_ID'; // ID 존재하지 않음
let RESULT_NOT_MATCH = 'NOT_MATCH'; // 일치하지 않음
let RESULT_NOT_MATCH_PW = 'NOT_MATCH_PW'; // PW 일치하지 않음
let RESULT_OVER_COUNT = 'OVER_COUNT'; // 회수 넘김
let RESULT_OVER_COUNT_PW = 'OVER_COUNT_PW'; // PW 회수 넘김
let RESULT_OVER_PERIOD = 'OVER_PERIOD'; // 기한 넘김
let RESULT_OVER_PERIOD_PW = 'OVER_PERIOD_PW'; // PW 기한 넘김
let RESULT_NEED_NEW_PW = 'NEED_NEW_PW'; // 새로운 비밀번호 필요
let RESULT_LOGOUT = 'LOGOUT'; // 로그아웃
let RESULT_NOT_AUTH = 'NOT_AUTH'; // 권한 없음
let RESULT_EMPTY = 'EMPTY'; // 비어있음
let RESULT_EMPTY_ID = 'EMPTY_ID'; // ID 비어있음
let RESULT_EMPTY_PW = 'EMPTY_PW'; // PW 비어있음




//**********************************************************************************
// CRUD 관련 - CommonDTO.java 의 멤버명과 동일하게 설정할것
//**********************************************************************************

let CRUD_TYPE = 'crudType'; // CRUD 구분



//**********************************************************************************
// 조회 관련 - SearchParamDTO.java 의 멤버명과 동일하게 설정할것
//**********************************************************************************

let SEARCH_PAGING_YN = 'pagingYn'; // 페이징 조회 여부
let SEARCH_PAGING_SHOW_YN = 'pagingShowYn'; // 페이징 노출 여부
let SEARCH_GRID_YN = 'gridYn'; // 그리드 조회 여부
let SEARCH_NOW_PAGE = 'nowPage'; // 현재 페이지
let SEARCH_SHOW_COUNT = 'showCount'; // 화면 그리드에 표시될 레코드수
let SEARCH_TOTAL_COUNT = 'totalCount'; // 총 검색결과 레코드수
let SEARCH_TOTAL_PAGE = 'totalPage'; // 총 페이지 수
let SEARCH_SORT_COL = 'sortCol'; // 정렬 컬럼
let SEARCH_SORT_TYPE = 'sortType'; // 정렬 타입 - ASC, DESC



//**********************************************************************************
// 레이아웃 관련
//**********************************************************************************

let LAYOUT = [];
let LAYOUT_PARENT = 'page_content'; // 기본적으로 찾는 부모 클래스명 : common.css 에서 정의
let LAYOUT_WRAP = 'layout_wrap'; // 레이아웃 최상위 클래스명 : 각각의 layout을 찾는 css키로 쓰인다
let LAYOUT_PARAM_WRAP = 'param_wrap'; // 엘리먼트 wrap 클래스명 : wrapping 및 require 처리를 위해 쓰인다
let LAYOUT_INFO_MSG = 'info_msg';
let LAYOUT_TITLE = 'layout_title'; // 레이아웃 타이틀



//**********************************************************************************
// 그리드 관련
//**********************************************************************************

let GRID = []; // 그리드 객체
let GRID_SORT_COL; // 그리드 정렬 컬럼

let PAGING_START_PAGE = 'pagingStartPage'; // 페이징의 페이지 개수별로 < > 버튼 생길때 세팅
let PAGING_END_PAGE = 'pagingEndPage'; // 페이징의 페이지 개수별로 < > 버튼 생길때 세팅
let PAGING_SHOW_PAGE = 'pagingShowPage'; // 화면에 보여질 페이징 개수
let PAGING_SHOW_PAGE_DEFAULT = 5; // 화면에 보여질 페이징 개수 기본값



//**********************************************************************************
// 팝업 관련
//**********************************************************************************

let POPUP = [];
let POPUP_WRAP = 'popup_wrap'; // 팝업 최상위 클래스명 : 각각의 popup을 찾는 css키로 쓰인다




//**********************************************************************************
// 키 관련
//**********************************************************************************

let KEY = {
	backspace: 8,
	tab: 9,
	enter: 13,
	shift: 16,
	ctrl: 17,
	alt: 18,
	pause: 19,
	capslock: 20,
	rightAlt: 21,
	rightCtrl: 25,
	esc: 27,
	space: 32,
	pageup: 33,
	pagedown: 34,
	end: 35,
	home: 36,
	left: 37,
	up: 38,
	right: 39,
	down: 40,
	insert: 45,
	'delete': 46,
}
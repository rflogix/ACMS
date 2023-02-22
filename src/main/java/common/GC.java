package common;

public class GC {
	// spring 세팅 관련
	public static final String[] RESOURCE_PATHS = {"js", "css", "image", "font"}; // 리소스 폴더
	public static final String MYBATIS_PATH = "classpath:mybatis"; // MybatisConfig.java 에서 설정되는 esources 에서 mybatis 경로
	
	// 보안관련
	public static final String ENCRYPT_KEY = "hkcmc-acms-rfl@gix"; // 암호화 비밀키 (최소 16자리)
	public static final int ENCRYPT_KEY_SIZE = 16; // 암호화 비밀키 크기
	public static final int PW_OVER_COUNT = 5; // 비밀번호 입력 제한 회수
	public static final int PW_SALT_SIZE = 16; // 비밀번호 SALT 크기 (16진수로 변경된 문자열로 바뀌기 때문에 DB에는 SALT_SIZE 의 2배로 잡아야 한다)
	
	// 파일관련
	public static final String FILE_TEMP_PATH = "$temp"; // 임시 폴더 경로

	// 쿠키관련
	public static final int COOKIE_TIME = 60 * 60 * 24; // 쿠키 유효 기간: 1일(60초 * 60분 * 24시간)
	public static final String COOKIE_USER_ID = "COOKIE_USER_ID";
	
	// 로그아웃 관련
	public static final String LOGOUT_URI = "/api/logout";

	// KEY 관련
	public static final String KEY_ERROR_CODE = "ERROR_CODE"; // ErrorConfig.java 의 model 세팅에 사용
	public static final String KEY_SESSION = "SESSION"; // GF.java 의 세션변수조회, 세션변수설정에 사용
	public static final String KEY_HOST = "host"; // RequestConfig.java 에서 설정되고 Controller 단에서 사용 : HttpHeaders.HOST 값(Host)과 다르다 
	public static final String KEY_SSO_YN = "SSO_YN"; // SSO 여부
	public static final String KEY_URL_ROOT = "URL_ROOT"; // URL_ROOT : http://aa.bb.com
	
	// KEY 관련 : page_script.jsp 에서 쓰이는 변수명과 동일하게 유지할것
	public static final String KEY_LOGIN_USER = "LOGIN_USER"; // 로그인 사용자 객체
	public static final String KEY_CONTEXT_PATH = "CONTEXT_PATH"; // context path
	
	// RESULT 코드 : constant.js 와 동일하게 유지할것
	public static final String SEARCH_PARAM = "searchParam";
	public static final String RESULT_CODE = "result";
	public static final String RESULT_MESSAGE = "message";
	public static final String RESULT_LIST = "list";
	public static final String RESULT_DTO = "dto";
	
	public static final String RESULT_OK = "OK"; // 성공
	public static final String RESULT_SUCCESS = RESULT_OK;
	public static final String RESULT_FAIL = "FAIL"; // 실패
	public static final String RESULT_ERROR = "ERROR"; // 에러
	public static final String RESULT_DUPLICATE = "DUPLICATE"; // 중복
	public static final String RESULT_NOT_NULL = "NOT_NULL"; // NULL 허용 안함
	public static final String RESULT_NOT_EXIST = "NOT_EXIST"; // 존재하지 않음
	public static final String RESULT_NOT_EXIST_ID = "NOT_EXIST_ID"; // ID 존재하지 않음
	public static final String RESULT_NOT_MATCH = "NOT_MATCH"; // 일치하지 않음
	public static final String RESULT_NOT_MATCH_PW = "NOT_MATCH_PW"; // PW 일치하지 않음
	public static final String RESULT_OVER_COUNT = "OVER_COUNT"; // 회수 넘김
	public static final String RESULT_OVER_COUNT_PW = "OVER_COUNT_PW"; // PW 회수 넘김
	public static final String RESULT_OVER_PERIOD = "OVER_PERIOD"; // 기한 넘김
	public static final String RESULT_OVER_PERIOD_PW = "OVER_PERIOD_PW"; // PW 기한 넘김
	public static final String RESULT_NEED_NEW_PW = "NEED_NEW_PW"; // 새로운 비밀번호 필요
	public static final String RESULT_LOGOUT = "LOGOUT"; // 로그아웃
	public static final String RESULT_NOT_AUTH = "NOT_AUTH"; // 권한 없음
	public static final String RESULT_EMPTY = "EMPTY"; // 비어있음
	public static final String RESULT_EMPTY_ID = "EMPTY_ID"; // ID 비어있음
	public static final String RESULT_EMPTY_PW = "EMPTY_PW"; // PW 비어있음
}
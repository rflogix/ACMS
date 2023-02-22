package acms.common;

public class GC extends common.GC {
	// URL 관련
	public static final String DOMAIN_URL = "acms.rflogix.com"; // application.yml 의 spring.config.name 과 동일하게 유지할것
	
	// 세션 관련
	public static final int SESSION_TIME = 1800; // 초단위이나 최소는 1분이므로 60초 이상
	
	// SSO
	public static final boolean SSO_SECURE_YN = false;
	public static final String SSO_URL = "sso.rflogix.com";
	
	// 공통코드
	public static final String CODE_AUTH_TYPE = "AUTH_TYPE"; // 권한 타입
	
	// SQL 에러코드
	public static final int SQL_ERROR_DUPLICATE = -10007; // PK 중복
	public static final int SQL_ERROR_NOT_NULL = -10005; // NOT NULL 필드
}
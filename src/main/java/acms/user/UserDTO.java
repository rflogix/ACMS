package acms.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import acms.common.CommonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class UserDTO extends CommonDTO {
	private long userSeq;
	private String userId;
	private String userNm;
	private String salt;
	private int menuType;
	
	// 로그인 히스토리
	private UserLoginDTO loginHistory;

	// 비밀번호
	private UserPasswordDTO userPassword;
	
	// 기타
	private String userPw; // 로그인 화면에서 사용
	private String userPwBefore; // 비밀번호 변경 화면에서 사용
	private int maxErrorCount; // 비밀번호 오류회수 초과한도
}

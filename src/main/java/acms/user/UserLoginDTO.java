package acms.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import acms.common.CommonDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
@SuppressWarnings("serial")
public class UserLoginDTO extends CommonDTO {
	private long userSeq;
	private int sno;
	
	private String ipAddress;
	private String browserInfo;
	private String loginDt;
	private String logoutDt;
}
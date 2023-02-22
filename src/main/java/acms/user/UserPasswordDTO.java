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
public class UserPasswordDTO extends CommonDTO {
	private long userSeq;
	private int sno;
	
	private String password;
	private String expireDt;
	private int errorCount;
}
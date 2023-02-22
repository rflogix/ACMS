package common.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiHistoryDTO {
	private long apiHistSeq;
	private long apiSendSeq;
	
	private String requestFrom;
	private String requestTo;
	private String requestIp;
	private String requestUri;
	private String requestParam;
	private String requestDt;
	private String responseParam;
	private String responseDt;
	private String responseTime;
}
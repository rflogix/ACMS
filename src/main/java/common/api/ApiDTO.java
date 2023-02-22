package common.api;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.MediaType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ApiDTO extends ApiHistoryDTO {
	// url 관련
	private String url; // url 우선 - http 까지 포함하는 주소
	private String requestUrl; // 따로 요청할 url 이 있을때 사용
	private String uri; // uri 만 들어올 경우 request 의 url 과 병합, request 가 없다면 uri 값 그대로
	private HttpServletRequest request;
	
	// header 관련
	private HashMap<String, Object> header;
	
	// method 관련
	private String method = "POST"; // POST(기본값) / GET
	
	// body 관련
	private HashMap<String, Object> param;
	public void addParam(String key, Object object) {
		if (param == null) {
			param = new HashMap<String, Object>();
		}
		param.put(key, object);
	}
	private MediaType contentType = MediaType.APPLICATION_JSON; // APPLICATION_JSON(기본값) / MULTIPART_FORM_DATA
}
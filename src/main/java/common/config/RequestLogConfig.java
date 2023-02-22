package common.config;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;

import common.GC;
import common.GF;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLogConfig implements HandlerInterceptor {
	@Value("${spring.config.name}")
	private String 도메인;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (log.isDebugEnabled()) { // 로깅이 DEBUG 일때만 수행
			// 리소스 경로는 파라미터 로그 남기지 않음
			if (GF.isResourceUri(request.getRequestURI()) == false) {
				
				// request 에서 param 가져오기
				HashMap<String, Object> map파라미터 = GF.getParameterMap(request);

				if (map파라미터.isEmpty() == false) {
					StringBuffer 파라미터_로그 = new StringBuffer();
					파라미터_로그.append("\n========== 파라미터 ==========");
					
					// 아이피
					String 아이피 = GF.getString(request.getHeader("X-FORWARDED-FOR"));
					if (아이피.equals("") == true) {
						아이피 = request.getRemoteAddr();
					}
					파라미터_로그.append("\n아이피 : "+아이피);
					
					// 로그인 사용자 정보
					try {
						HashMap<String, Object> map로그인사용자 = new HashMap<String, Object>();
						map로그인사용자.put("userSeq", 0L);
						HttpSession session = request.getSession();
						if (session.getAttribute(GC.KEY_LOGIN_USER) != null) {
							Object loginUser = session.getAttribute(GC.KEY_LOGIN_USER);
							Class<?> userDTO = loginUser.getClass();
							Method 클래스함수 = userDTO.getMethod("getUserSeq");
							클래스함수.setAccessible(true);
							long userCD = (long)클래스함수.invoke(loginUser);
							if (userCD != 0) {
								map로그인사용자.put("userSeq", userCD);
								
								클래스함수 = userDTO.getMethod("getUserId");
								클래스함수.setAccessible(true);
								map로그인사용자.put("userId", 클래스함수.invoke(loginUser));
								
								클래스함수 = userDTO.getMethod("getUserNm");
								클래스함수.setAccessible(true);
								map로그인사용자.put("userNm", 클래스함수.invoke(loginUser));
							}
						}
						if ((long)map로그인사용자.get("userSeq") != 0) {
							파라미터_로그.append("\n로그인사용자 : "+map로그인사용자.get("userNm")+" ("+map로그인사용자.get("userId")+" / "+map로그인사용자.get("userSeq")+")");
						}
						
					} catch(Exception e) {
						
					}
					
					// 비밀번호 로그 * 처리
					/*String arrPW[] = {"userPW", "PW", "password", "pass"};
					for (String key : arrPW) {
						if (GlobalFunction.getString(map.get(key)).equals("") == false) {
							map파라미터.put(key, "****");
						}
					}*/
					
					파라미터_로그.append("\n요청URI : "+request.getRequestURI());
					파라미터_로그.append("\n요청PARAM :\n"+GF.getJsonPretty(map파라미터));
					파라미터_로그.append("\n==============================");
					log.debug("{}", 파라미터_로그.toString());
				}
			}
		}
		
		return true;
	}
}
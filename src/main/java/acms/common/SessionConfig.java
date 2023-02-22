package acms.common;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.annotation.Configuration;
import acms.user.UserDTO;
import common.api.ApiDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SessionConfig implements HttpSessionListener {
	
	// 세션 종료시 로그아웃 처리
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			HttpSession session = se.getSession();
			
			// 로그인 되어 있는지 체크하여
			UserDTO loginUser = (UserDTO)session.getAttribute(GC.KEY_LOGIN_USER);
			if (GF.getLoginYn(loginUser)) {
				// 로그아웃 uri 호출
				ApiDTO apiParam = new ApiDTO();
				apiParam.setUrl(GF.getString(session.getAttribute(GC.KEY_URL_ROOT)) + GC.LOGOUT_URI);
				apiParam.addParam("userSeq", loginUser.getUserSeq());
				apiParam.addParam("sno", loginUser.getLoginHistory().getSno());
				GF.sendApi(apiParam);
				
				log.debug("[{} / {}] 님이 로그아웃 하였습니다 (세션ID : {})", loginUser.getUserNm(), loginUser.getUserId(), session.getId());
			}
			
		} catch(Exception e) {
			log.debug("{}", e.toString());
		}
	}
}
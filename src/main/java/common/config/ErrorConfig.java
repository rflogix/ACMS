package common.config;

import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import common.GC;

@Controller
public class ErrorConfig implements ErrorController {
	@Value("${server.error.path}") // application.yml
	private String 에러경로;
	
	@RequestMapping("/**${server.error.path}") // 에러경로
	public ModelAndView handleError(HttpServletResponse response) {
		HashMap<String, Object> model = new HashMap<String, Object>();
		model.put(GC.KEY_ERROR_CODE, ""+response.getStatus());
		switch (response.getStatus()){
			case HttpServletResponse.SC_UNAUTHORIZED: // 비승인
				model.put(GC.KEY_ERROR_CODE, GC.RESULT_NOT_AUTH);
				break;
				
			case HttpServletResponse.SC_FORBIDDEN: // 숨겨졌거나
			case HttpServletResponse.SC_NOT_FOUND: // 없는 페이지
				model.put(GC.KEY_ERROR_CODE, GC.RESULT_NOT_EXIST);
				break;
			
			case HttpServletResponse.SC_INTERNAL_SERVER_ERROR: // 서버 에러
				model.put(GC.KEY_ERROR_CODE, GC.RESULT_ERROR);
				break;
		}
		return new ModelAndView(에러경로, model);
	}
}
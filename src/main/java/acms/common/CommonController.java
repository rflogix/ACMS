package acms.common;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import acms.user.UserService;

@Controller
@RequestMapping(headers={GC.KEY_HOST + "=" + GC.DOMAIN_URL})
public class CommonController {
	@Autowired private CommonService commonService;
	@Autowired private UserService userService;
	
	
	
	
	//**************************************************************************
	// index 페이지
	//**************************************************************************
	
	@RequestMapping(value={"", "/", "/index", "/index.do"})
	public String 인덱스_페이지(HttpServletRequest request, Model model) throws Exception {
		return commonService.goToPage(request, model, "/index");
	}
	
	
	
	
	//**************************************************************************
	// home 페이지
	//**************************************************************************
	
	@RequestMapping(value={"/home"})
	public String 홈_페이지(HttpServletRequest request, Model model) throws Exception {
		return "home";
	}
	
	
	
	
	//**************************************************************************
	// 로그아웃 - api (수동 로그아웃 및 세션만료시 로그아웃 공용)
	//**************************************************************************
	
	@RequestMapping(GC.LOGOUT_URI)
	public @ResponseBody HashMap<String, Object> 로그아웃(@RequestBody HashMap<String, Object> mapBody, HttpServletRequest request) throws Exception {
		userService.logoutHistory(mapBody);
		return null;
	}
	
	
	
	
	//**************************************************************************
	// swagger
	//**************************************************************************
	
	@RequestMapping("/swagger")
	public String 스웨거(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "redirect:/swagger-ui/index.html#/ACMS-Api";
	}
	
	
	
	
	//**************************************************************************
	// 그리드 다운로드
	//**************************************************************************
	
	@RequestMapping("/grid/download")
	public ResponseEntity<?> 그리드다운로드(@RequestParam HashMap<String, Object> mapParam) throws Exception {
		return commonService.gridExcelDownload(mapParam);
	}
	
}
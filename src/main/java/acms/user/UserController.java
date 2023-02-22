package acms.user;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import acms.common.CommonService;
import acms.common.GC;

@Controller
@RequestMapping(headers={GC.KEY_HOST + "=" + GC.DOMAIN_URL})
public class UserController {
	@Autowired private CommonService commonService;
	@Autowired private UserService userService;
	
	
	
	
	//**************************************************************************
	// 페이지
	//**************************************************************************
	
	@RequestMapping("/login")
	public String 로그인_페이지(HttpServletRequest request, Model model) throws Exception {
		return commonService.goToPage(request, model, "/login");
	}
	
	@RequestMapping("/user")
	public String 사용자관리_페이지(HttpServletRequest request, Model model) throws Exception {
		return commonService.goToPage(request, model, "/user_summary");
	}
	
	
	
	
	//**************************************************************************
	// API
	//**************************************************************************

	@RequestMapping("/user/search")
	public @ResponseBody HashMap<String, Object> 조회(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request) throws Exception {
		return userService.search(mapParam, request);
	}
	
	@RequestMapping("/user/save")
	public @ResponseBody HashMap<String, Object> 저장(@RequestBody HashMap<String, Object> mapBody, HttpServletRequest request) throws Exception {
		return userService.save(mapBody, request);
	}
	
	@RequestMapping("/user/login")
	public @ResponseBody HashMap<String, Object> 로그인(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return userService.login(mapParam, request, response);
	}
	
	@RequestMapping("/user/logout")
	public @ResponseBody HashMap<String, Object> 로그아웃(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return userService.logout(mapParam, request, response);
	}
	
	@RequestMapping("/user/changePswd")
	public @ResponseBody HashMap<String, Object> 비밀번호변경(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request) throws Exception {
		return userService.changePassword(mapParam, request);
	}
}
package acms.menu;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

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
public class MenuController {
	@Autowired private CommonService commonService;
	@Autowired private MenuService menuService;
	
	
	
	
	//**************************************************************************
	// 페이지
	//**************************************************************************
	
	@RequestMapping("/menu")
	public String 메뉴관리_페이지(HttpServletRequest request, Model model) throws Exception {
		return commonService.goToPage(request, model, "/menu_summary");
	}
	
	
	
	
	//**************************************************************************
	// API
	//**************************************************************************
	
	@RequestMapping("/menu/search")
	public @ResponseBody HashMap<String, Object> 조회(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request) throws Exception {
		return menuService.search(mapParam, request);
	}
	
	@RequestMapping("/menu/save")
	public @ResponseBody HashMap<String, Object> 저장(@RequestBody HashMap<String, Object> mapBody, HttpServletRequest request) throws Exception {
		return menuService.save(mapBody, request);
	}
	
	@RequestMapping("/menu/maxOrder")
	public @ResponseBody HashMap<String, Object> 순서MAX값조회(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request) throws Exception {
		return menuService.maxOrder(mapParam, request);
	}
	
	@RequestMapping("/menu/tree")
	public @ResponseBody List<HashMap<String, Object>> 트리조회(@RequestParam HashMap<String, Object> mapParam, HttpServletRequest request) throws Exception {
		return menuService.tree(mapParam, request);
	}
}
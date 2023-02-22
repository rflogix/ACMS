package test.common;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(headers={GC.KEY_HOST + "=" + GC.DOMAIN_URL})
public class CommonController {
	
	//**************************************************************************
	// INDEX 페이지
	//**************************************************************************
	
	@RequestMapping(value={"", "/", "/index", "/index.do"})
	public String 인덱스_페이지(HttpServletRequest request, Model model) throws Exception {
		return "index";
	}
	
}
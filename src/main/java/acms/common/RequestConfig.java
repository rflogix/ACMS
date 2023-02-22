package acms.common;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import acms.user.UserDTO;
import acms.user.UserService;

@Configuration
public class RequestConfig implements Filter {
	@Autowired private UserService userService;
	@Value("${spring.config.name}")
	private String 도메인;
	
	@Override
	public void doFilter(ServletRequest serveletRequest, ServletResponse serveletResponse, FilterChain chain) throws IOException, ServletException {
		try {
			// request parameter 에 매번 추가 / 변경 / 복호화 해야할 파라미터 세팅
			RequestCustom request = new RequestCustom((HttpServletRequest) serveletRequest);
			if (GF.isResourceUri(request.getRequestURI()) == false) { // 리소스 아닐때만 필터링
				
				// 세션 설정하기
				HttpSession session = request.getSession();
				session.setMaxInactiveInterval(GC.SESSION_TIME);
				
				// 로그인 사용자 설정
				if (GF.getLoginYn(session)) { // 로그인 상태라면 다시 조회하여 세팅한다 (변동내역 반영)
					UserDTO loginUser = (UserDTO)session.getAttribute(GC.KEY_LOGIN_USER);
					loginUser = userService.selectPK(loginUser.getUserSeq());
					session.setAttribute(GC.KEY_LOGIN_USER, loginUser);
					request.setAttribute(GC.KEY_LOGIN_USER, GF.getJson(loginUser)); // page_script.jsp 에서 사용
				}
				
				// CONTEXT_PATH 설정
				String contextPath = request.getContextPath();
				if (contextPath.equals("")) {
					contextPath = "/";
				}
				request.setAttribute(GC.KEY_CONTEXT_PATH, contextPath); // page_script.jsp 에서 사용
				
				// Controller 용 도메인 세팅
				String host = GF.getString(request.getHeader(GC.KEY_HOST)) + "," + GF.getString(request.getHeader(HttpHeaders.HOST));
				if (host.indexOf("localhost") > -1) {
					request.setHeader(GC.KEY_HOST, 도메인); // 모든 컨트롤러 최상단에서 사용
				}
				
				chain.doFilter(request, serveletResponse);
				
			} else {
				chain.doFilter(serveletRequest, serveletResponse);
			}
			
		} catch (Exception e) {
			chain.doFilter(serveletRequest, serveletResponse);
		}
	}

	private static class RequestCustom extends HttpServletRequestWrapper {
		Map<String, String[]> map파라미터;
		Map<String, LinkedList<String>> mapHeader;
		
		public RequestCustom(HttpServletRequest request) {
			super(request);
			
			// 파라미터 초기화
			this.map파라미터 = new HashMap<String, String[]>(request.getParameterMap()); // 기존 request 값을 수정가능한 map 으로 새로 생성
			
			// 헤더 초기화
			this.mapHeader = new HashMap<String, LinkedList<String>>();  
			Enumeration<String> keys = request.getHeaderNames();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				LinkedList<String> values = new LinkedList<String>();
				Enumeration<String> headerValues = request.getHeaders(key);
				while (headerValues.hasMoreElements()) {
					values.add(headerValues.nextElement());
				}
				this.mapHeader.put(key, values);
			}
		}
		
		// 파라미터 오버라이딩
		@Override
		public String getParameter(String key) {
			String value = null;
			String[] values = this.getParameterValues(key);
			if (values != null && values.length > 0) {
				value = values[0];
			}
			return value;
		}

		@Override
		public String[] getParameterValues(String key) {
			String[] values = null;
			String[] temp = this.map파라미터.get(key);
			if (temp != null) {
				values = new String[temp.length];
				// values = GF.파라미터암호화(values);
				System.arraycopy(temp, 0, values, 0, temp.length);
			}
			return values;
		}

		@Override
		public Map<String, String[]> getParameterMap() {
			return Collections.unmodifiableMap(this.map파라미터);
		}
		
		@Override
		public Enumeration<String> getParameterNames() {
			return Collections.enumeration(this.map파라미터.keySet());
		}

		// 아래 정의된 setParameter 함수가 Custom Parameter 추가/수정 가능하게 함
		@SuppressWarnings("unused")
		public void setParameter(String key, String value) {
			String[] oneParam = {value};
			setParameter(key, oneParam);
		}
		public void setParameter(String key, String[] values) {
			this.map파라미터.put(key, values);
		}
		
		// Header 오버라이딩
		@Override
		public String getHeader(String key) {
			String value = "";
			
			LinkedList<String> values = this.mapHeader.get(key);
			if (values != null) {
				if (values.size() > 0) {
					value = values.get(0);
				}
			}
			
			return value;
		}
		
		@Override
		public Enumeration<String> getHeaders(String key) {
			Vector<String> list = new Vector<String>();
			
			if (this.mapHeader != null) {
				LinkedList<String> values = this.mapHeader.get(key);
				if (values != null) {
					for (String value : values) {
						list.addElement(value);
					}
				}
			}
			
			return list.elements();
		}
		
		@Override
		public Enumeration<String> getHeaderNames() {
			Vector<String> list = new Vector<String>();
			
			if (this.mapHeader != null) {
				if (this.mapHeader.keySet() != null) {
					for (String value : this.mapHeader.keySet()) {
						list.addElement(value);
					}
				}
			}
			
			return list.elements();
		}
		
		// 아래 정의된 setHeader 함수가 Custom Header 추가/수정 가능하게 함
		public void setHeader(String key, String value) {
			LinkedList<String> values = new LinkedList<String>();
			values.add(value);
			this.mapHeader.put(key, values);
		}
		
	}

	public void destroy() {}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}
}

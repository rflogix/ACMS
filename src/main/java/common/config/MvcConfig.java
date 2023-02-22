package common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	// 인터셉터 설정
	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(new RequestLogConfig()); // 파라미터 로그 세팅
	}
}
package common.config;

import java.net.MalformedURLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = {"common", "acms", "test"}, nameGenerator = BeanNameConfig.class) // @SpringBootConfiguration + @EnableAutoConfiguration + @ComponentScan 조합
public class SpringBootMain extends SpringBootServletInitializer {
	
	public static void main(String[] args) throws MalformedURLException {
		SpringApplication springApplication = new SpringApplication(SpringBootMain.class);
		springApplication.setLogStartupInfo(false);
		springApplication.run(args);

		log.warn("{}", "█████ SPRING BOOT 시작 █████");
	}

	// 외부 WAS 로 돌리기 위해 아래 코드 추가
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SpringBootMain.class);
	}
}

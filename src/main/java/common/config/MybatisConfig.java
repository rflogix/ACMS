package common.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

import common.GC;

public class MybatisConfig {
	// 도메인별로 DB 접속 정보가 상이하므로 application.yml 의 custom.mybatis.[package] 항목에 도메인별로 세팅
	public static final String 패키지1 = "acms"; // 패키지1 에는 @Primary 추가
	public static final String 패키지2 = "test"; // 패키지2부터는 @Primary 제외
}


//****************************************************************************
// 패키지1
//****************************************************************************

@Configuration
@MapperScan(basePackages = {MybatisConfig.패키지1}, sqlSessionFactoryRef = "SqlSessionFactory_" + MybatisConfig.패키지1, nameGenerator = BeanNameConfig.class)
class MybatisConfig_패키지1 {
	@Value("${spring.datasource.hikari.driverClassName}") // application.yml
	private String driverClassName;
	@Value("${spring.datasource.hikari.jdbcUrl}") // application.yml
	private String jdbcUrl;
	@Value("${spring.datasource.hikari.username}") // application.yml
	private String username;
	@Value("${spring.datasource.hikari.password}") // application.yml
	private String password;
	
	@Primary
	@Bean(name = "DataSource_" + MybatisConfig.패키지1)
	public DataSource DataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class)
			.driverClassName(driverClassName)
			.url(jdbcUrl)
			.username(username)
			.password(password)
			.build();
	}
	
	@Primary
	@Bean(name = "SqlSessionFactory_" + MybatisConfig.패키지1)
	public SqlSessionFactory SqlSessionFactory(@Qualifier("DataSource_" + MybatisConfig.패키지1) DataSource dataSource) throws Exception {
		String 패키지 = MybatisConfig.패키지1;
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage(패키지);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(GC.MYBATIS_PATH + "/" + 패키지 + "/**/*.xml"));
		
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true); // 언더바를 카멜케이스로
		sqlSessionFactory.getConfiguration().setCallSettersOnNulls(false); // 필드가 null인 경우도 필드 누락 안되도록
		sqlSessionFactory.getConfiguration().setJdbcTypeForNull(JdbcType.NULL); // 쿼리에 보내는 파라미터가 null 일경우 오류 발생 방지
		return sqlSessionFactory;
	}
	
	@Primary
	@Bean(name = "SqlSessionTemplate_" + MybatisConfig.패키지1)
	public SqlSession SqlSessionTemplate(@Qualifier("SqlSessionFactory_" + MybatisConfig.패키지1) SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Primary
	@Bean(name = "DataSourceTransactionManager_" + MybatisConfig.패키지1)
	public DataSourceTransactionManager DataSourceTransactionManager(@Qualifier("DataSource_" + MybatisConfig.패키지1) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}




//****************************************************************************
// 패키지2
//****************************************************************************

@Configuration
@MapperScan(basePackages = {MybatisConfig.패키지2}, sqlSessionFactoryRef = "SqlSessionFactory_" + MybatisConfig.패키지2, nameGenerator = BeanNameConfig.class)
class MybatisConfig_패키지2 {
	@Value("${spring.datasource.hikari.driverClassName}") // application.yml
	private String driverClassName;
	@Value("${spring.datasource.hikari.jdbcUrl}") // application.yml
	private String jdbcUrl;
	@Value("${spring.datasource.hikari.username}") // application.yml
	private String username;
	@Value("${spring.datasource.hikari.password}") // application.yml
	private String password;
	
	//@Primary
	@Bean(name = "DataSource_" + MybatisConfig.패키지2)
	public DataSource DataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class)
			.driverClassName(driverClassName)
			.url(jdbcUrl)
			.username(username)
			.password(password)
			.build();
	}
	
	//@Primary
	@Bean(name = "SqlSessionFactory_" + MybatisConfig.패키지2)
	public SqlSessionFactory SqlSessionFactory(@Qualifier("DataSource_" + MybatisConfig.패키지2) DataSource dataSource) throws Exception {
		String 패키지 = MybatisConfig.패키지2;
		
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage(패키지);
		sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(GC.MYBATIS_PATH + "/" + 패키지 + "/**/*.xml"));
		
		SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
		sqlSessionFactory.getConfiguration().setMapUnderscoreToCamelCase(true); // 언더바를 카멜케이스로
		sqlSessionFactory.getConfiguration().setCallSettersOnNulls(false); // 필드가 null인 경우도 필드 누락 안되도록
		sqlSessionFactory.getConfiguration().setJdbcTypeForNull(JdbcType.NULL); // 쿼리에 보내는 파라미터가 null 일경우 오류 발생 방지
		return sqlSessionFactory;
	}
	
	//@Primary
	@Bean(name = "SqlSessionTemplate_" + MybatisConfig.패키지2)
	public SqlSession SqlSessionTemplate(@Qualifier("SqlSessionFactory_" + MybatisConfig.패키지2) SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	//@Primary
	@Bean(name = "DataSourceTransactionManager_" + MybatisConfig.패키지2)
	public DataSourceTransactionManager DataSourceTransactionManager(@Qualifier("DataSource_" + MybatisConfig.패키지2) DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
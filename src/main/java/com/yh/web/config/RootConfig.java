package com.yh.web.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.jasypt.spring31.properties.EncryptablePropertyPlaceholderConfigurer;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration //설정용 클래스
@EnableTransactionManagement //<tx:annotation-driven />
@ComponentScan(basePackages = { "com.yh.web.dao", "com.yh.web.service"})
public class RootConfig {
	private static final Logger logger = LoggerFactory.getLogger(RootConfig.class);
	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String jdbcUrl;
	@Value("${jdbc.username}")
	private String userName;
	@Value("${jdbc.password}")
	private String password; 
	
	@Bean
	public static EnvironmentStringPBEConfig encryptorConfig() {
	   EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
	   config.setAlgorithm("PBEWithMD5AndDES");
	   config.setPassword("!kang1234@");
	   return config;
	}
	@Bean
	public static StandardPBEStringEncryptor encryptor() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
	   encryptor.setConfig(encryptorConfig());
	   return encryptor;
	}	
	
	@Bean
	public static EncryptablePropertyPlaceholderConfigurer propertyConfigurer() throws IOException {
		EncryptablePropertyPlaceholderConfigurer propertyConfigurer = new EncryptablePropertyPlaceholderConfigurer(encryptor());
		propertyConfigurer.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:config/*.properties"));
		return propertyConfigurer;
	}
	
	@Bean
	public DataSource dataSource() {	
		PooledDataSource dataSource = new PooledDataSource();
		dataSource.setDriver(driverClassName);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(userName);
		dataSource.setPassword(password);
		return dataSource;
	}


	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/model/modelConfig.xml"));
		sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mappers/*.xml"));
		return sessionFactory.getObject();
	}
	
	@Bean  // SqlSession구현
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}	
	
     //트랜잭션 관리
	@Bean
	public DataSourceTransactionManager txManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
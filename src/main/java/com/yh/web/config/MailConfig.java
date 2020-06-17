package com.yh.web.config;

import java.util.Properties;

import javax.net.ssl.SSLSocketFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {
	@Value("${mail.password}")
	private String password;
	
	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl mailSender = new  JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("yohaniayo@gmail.com");
		mailSender.setPassword(password);
		Properties props = new Properties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);	
		props.put("mail.smtp.socketFactory.class", SSLSocketFactory.class);
		props.put("mail.debug", true);
		mailSender.setJavaMailProperties(props);
		return mailSender;
	}
}

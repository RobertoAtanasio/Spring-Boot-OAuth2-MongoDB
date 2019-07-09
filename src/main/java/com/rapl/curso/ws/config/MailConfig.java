package com.rapl.curso.ws.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
//@PropertySource("classpath:application.properties")
@PropertySource(value = { "file:\\C:\\opt\\.sba7-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {

	@Autowired
	private Environment env;
	
	@Bean
	public JavaMailSender javaMailSender() {
		Properties props = new Properties();
		props.put("mail.transport.protocol", env.getProperty("mail.transport.protocol"));
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.ssl.trust", env.getProperty("mail.smtp.ssl.trust"));
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.connectiontimeout", Integer.parseInt(env.getProperty("mail.smtp.connectiontimeout")));	// em mili segundos
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(props);
		mailSender.setHost(env.getProperty("HOST"));
		mailSender.setPort(Integer.parseInt(env.getProperty("PORT")));		
		mailSender.setUsername(env.getProperty("USUARIO"));
		mailSender.setPassword(env.getProperty("SENHA"));
		
		return mailSender;
	}
}

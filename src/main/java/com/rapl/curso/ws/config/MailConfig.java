package com.rapl.curso.ws.config;

import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;

@Configuration
@PropertySource("classpath:application.properties")
//@PropertySource(value = { "file:\\C:\\opt\\.sba7-mail.properties" }, ignoreResourceNotFound = true)
public class MailConfig {

	private static final Logger LOG = LoggerFactory.getLogger(MailConfig.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Bean
	public JavaMailSender javaMailSender() {
		Properties props = new Properties();
		props.put("mail.transport.protocol", env.getProperty("mail.transport.protocol"));
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.ssl.trust", env.getProperty("mail.smtp.ssl.trust"));
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.ssl.enable", true);
		props.put("mail.smtp.socketFactory.port", 465);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.connectiontimeout", Integer.parseInt(env.getProperty("mail.smtp.connectiontimeout"))); // em
																													// mili
																													// segundos

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(props);
		mailSender.setHost(env.getProperty("HOST"));
		mailSender.setPort(Integer.parseInt(env.getProperty("PORT")));
		mailSender.setUsername(env.getProperty("USUARIO"));
		mailSender.setPassword(env.getProperty("SENHA"));

		return mailSender;
	}

	@Async
	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			LOG.info("Enviando Email!");
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true); // true - mensagem em html

			javaMailSender.send(mimeMessage);
			LOG.info("Email enviado!");
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}
}

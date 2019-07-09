package com.rapl.curso.ws.services.email;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.rapl.curso.ws.domain.Usuario;

@Component
public class Mailer {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private TemplateEngine thymeleaf;

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String template,
			Map<String, Object> variaveis) {
		Context context = new Context(new Locale("pt", "BR"));
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		String mensagem = thymeleaf.process(template, context);
		this.enviarEmail(remetente, destinatarios, assunto, mensagem);
	}

	public void enviarEmail(String remetente, List<String> destinatarios, String assunto, String mensagem) {
		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true); // true - mensagem em html

			javaMailSender.send(mimeMessage);
		} catch (MessagingException e) {
			throw new RuntimeException("Problemas com o envio de e-mail!", e);
		}
	}
	
	public void avisarSobreUsuariosNaoLiberados(List<Usuario> usuarios, List<Usuario> destinatarios) {
		
		Map<String, Object> variaveis = new HashMap<>();
		
		variaveis.put("usuarios", usuarios);	// incluir a lista de lançamentos vencidos
		
		List<String> emailsDestinatarios = destinatarios.stream()
				.map(u -> "roberto.atanasio.pl@gmail.com")
				.collect(Collectors.toList());
		
//		List<String> emailsDestinatarios = destinatarios.stream()
//				.map(u -> u.getEmail())
//				.collect(Collectors.toList());
		
		String remetente = "roberto.atanasio.pl@gmail.com";
		String assunto = "Usuários não liberados";
		String template = "mail/aviso-lancamentos-vencidos";
		
		this.enviarEmail(
				remetente, 
				emailsDestinatarios, 
				assunto, 
				template, 
				variaveis);
	}
	
//--- script para testar o envio do email
	
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		this.enviarEmail("roberto.atanasio.pl@gmail.com", 
//				Arrays.asList("roberto.atanasio.pl@gmail.com"), 
//				"Testando", "Olá!<br/>Teste ok.");
//		System.out.println("Terminado o envio de e-mail...");
//	}
	
//--- script para testar o evio do email
	
//	@Autowired
//	private LancamentoRepository repo;
//	
//	//--- o @EventListener está escultando o evento ApplicationReadyEvent
//	@EventListener
//	private void teste(ApplicationReadyEvent event) {
//		
//		String template = "mail/aviso-lancamentos-vencidos";  // a pasta templstes não é necessária porque o thymeleaf
//															  // já assume que ela exista
//		
//		List<Lancamento> lista = repo.findAll(); // acessou todos apenas para testar
//
//		Map<String, Object> variaveis = new HashMap<>();
//		
//		variaveis.put("lancamentos", lista); // lancamentos é o nome que está no html
//		
//		this.enviarEmail("roberto.atanasio.pl@gmail.com", 
//				Arrays.asList("roberto.atanasio.pl@gmail.com"), 
//				"Testando", template, variaveis);
//		
//		System.out.println("Terminado o envio de e-mail...");
//	}
}

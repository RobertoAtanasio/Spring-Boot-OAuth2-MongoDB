package com.rapl.curso.ws.services.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.rapl.curso.ws.config.MailConfig;
import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.domain.VerificacaoToken;
import com.rapl.curso.ws.services.UsuarioService;

@Component
public class Mailer {

	@Autowired
	private TemplateEngine thymeleaf;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private MailConfig mailConfig;
	
	@Value("${default.sender}")
	private String sender;
	
	@Value("${default.url}")
	private String contextPath;
	
	public void enviarEmailRequisicaoConfirmacao(Usuario usuario, VerificacaoToken vToken) {
		Map<String, Object> variaveis = new HashMap<>();
		
		String remetente = this.sender;
		String assunto = "Confirmação de Registro";
		List<String> listaEmails = new ArrayList<>();
		listaEmails.add(usuario.getEmail());	
		
		String token = UUID.randomUUID().toString();
		if (vToken == null) {
			this.usuarioService.criarVerificacaoTokenParaUsuario(usuario, token);
		} else {
			token = vToken.getToken();
		}
		
		String confirmationUrl = this.contextPath + "/api/public/regitrationConfirm/users?token=" + token;		

		Context context = new Context(new Locale("pt", "BR"));
		context.setVariable("user", usuario);
		context.setVariable("confirmationUrl", confirmationUrl);
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		String template = thymeleaf.process("email/registerUser", context);
		
		mailConfig.enviarEmail(remetente, listaEmails, assunto, template);
	}
	
	public void avisarSobreUsuariosNaoLiberados(List<Usuario> usuarios, List<Usuario> destinatarios) {
		
		Map<String, Object> variaveis = new HashMap<>();
		
		variaveis.put("usuarios", usuarios);	// incluir a lista de lançamentos vencidos
		
		List<String> listaEmails = destinatarios.stream()
				.map(u -> "roberto.atanasio.pl@gmail.com")
				.collect(Collectors.toList());
		
//		List<String> emailsDestinatarios = destinatarios.stream()
//				.map(u -> u.getEmail())
//				.collect(Collectors.toList());
		
		String remetente = "roberto.atanasio.pl@gmail.com";
		String assunto = "Usuários não liberados";
		
		Context context = new Context(new Locale("pt", "BR"));
		variaveis.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		String template = thymeleaf.process("mail/aviso-lancamentos-vencidos", context);
		
		mailConfig.enviarEmail(remetente, listaEmails, assunto, template);
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

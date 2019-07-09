package com.rapl.curso.ws.services.email;

import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.domain.VerificacaoToken;
import com.rapl.curso.ws.services.UsuarioService;
import com.rapl.curso.ws.services.exception.ObjetoNaoEncontradoException;

@PropertySource(value = { "file:\\C:\\opt\\.sba7-mail.properties" }, ignoreResourceNotFound = true)
public abstract class AbstractEmailService implements EmailService{
	
	@Autowired
	private Environment env;
	
	@Value("${default.sender}")
    private String sender;
	
    @Value("${default.url}")
    private String contextPath;
    
    @Autowired
    private TemplateEngine templateEngine;
    
    @Autowired
    private JavaMailSender javaMailSender;
    
    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void sendConfirmationHtmlEmail(Usuario usuario, VerificacaoToken vToken){
        try {
            MimeMessage mimeMessage = prepareMimeMessageFromUser(usuario, vToken);            
            sendHtmlEmail(mimeMessage);
        } catch (MessagingException msg) {
            throw new ObjetoNaoEncontradoException(String.format("Erro ao tentar enviar o e-mail"));
        }
    }

    protected MimeMessage prepareMimeMessageFromUser(Usuario usuario, VerificacaoToken vToken) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(usuario.getEmail());
        
//        mimeMessageHelper.setFrom(this.sender);
        mimeMessageHelper.setFrom(env.getProperty("default.sender"));
        
        mimeMessageHelper.setSubject("Confirmação de Registro");
        mimeMessageHelper.setSentDate(new Date((System.currentTimeMillis())));
        mimeMessageHelper.setText(htmlFromTemplateUser(usuario,vToken), true);
        return mimeMessage;
    }

    protected String htmlFromTemplateUser(Usuario usuario, VerificacaoToken vToken){
        String token = UUID.randomUUID().toString();
        if (vToken == null) {
            this.usuarioService.criarVerificacaoTokenParaUsuario(usuario, token);
        } else {
            token = vToken.getToken();
        }
//        String confirmationUrl = this.contextPath + "/api/public/regitrationConfirm/users?token="+token;
        String confirmationUrl = env.getProperty("default.url") + "/api/public/regitrationConfirm/users?token="+token;
        Context context = new Context();
        context.setVariable("user", usuario);
        context.setVariable("confirmationUrl", confirmationUrl);
        return templateEngine.process("email/registerUser", context);
    }

}

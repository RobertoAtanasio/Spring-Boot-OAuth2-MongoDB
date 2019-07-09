package com.rapl.curso.ws.services.email;

import javax.mail.internet.MimeMessage;

import com.rapl.curso.ws.domain.Usuario;
import com.rapl.curso.ws.domain.VerificacaoToken;

// em vez de incluir as anotações @Configuration e @PropertySource("classpath:application.properties") nesta classe, foi
// criado a classe EmailConfig e colocado as anotações nessa classe.

//@Configuration
//@PropertySource("classpath:application.properties")
public interface EmailService {

	void sendHtmlEmail(MimeMessage msg);
    void sendConfirmationHtmlEmail(Usuario ususario, VerificacaoToken vToken);
}

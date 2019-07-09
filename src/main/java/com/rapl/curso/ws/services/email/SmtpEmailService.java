//package com.rapl.curso.ws.services.email;
//
//import javax.mail.internet.MimeMessage;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//
//public class SmtpEmailService extends AbstractEmailService {
//
//	@Autowired
//    private JavaMailSender javaMailSender;
//
//    private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class);
//
//    @Override
//    public void sendHtmlEmail(MimeMessage msg) {
//        LOG.info("Enviando Email!");
//        javaMailSender.send(msg);
//        LOG.info("Email enviado com sucesso!");
//    }
//
//}

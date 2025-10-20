package com.recsys.recPet.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final String MAIL_SUBJECT = "Recuperar Senha!";

    @Autowired
    private Environment environment;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${front.url}")
    private String urlFront;

    public void sendSimpleEmail(String emailDestinatario, String nomeDestinatario, String token)
            throws MessagingException, UnsupportedEncodingException {

        String mailFrom = environment.getProperty("spring.mail.username");
        String mailFromName = environment.getProperty("mail.from.name", "RecPet");

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper email = new MimeMessageHelper(mimeMessage, "UTF-8");

        email.setTo(emailDestinatario);
        email.setSubject(MAIL_SUBJECT);
        email.setFrom(new InternetAddress(mailFrom, mailFromName));

        String link =  this.urlFront + "/recuperar-senha?token=" + token;

        String corpo = String.format(
                "Olá, %s!\n" +
                        "\n" +
                        "Recebemos seu pedido para recuperação de senha. " +
                        "Clique no link abaixo para redefinir sua senha:\n\n%s\n\nEquipe RecPet",
                nomeDestinatario,
                link
        );

        email.setText(corpo, false);
        mailSender.send(mimeMessage);
    }
}

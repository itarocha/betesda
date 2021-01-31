package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.EmailDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

	// VM Options:  -Dspring.profiles.active=local

	//private final JavaMailSender mailSender;
	private final MailGunService ms;

	@Value("${application.link.reset}")
	private String resource;

	public void solicitarSenha(String emailDestinatario, String nome, String token) {
		
        String link = String.format("%s%s", resource, token);

        //String link = String.format("%s/criar-senha/%s/",url, token);
        
		String from  = "Petra Sistemas <postmaster@petrasistemas.com>";
		String to = emailDestinatario;
		String subject = "Criação de nova senha";

		EmailTemplate template = new EmailTemplate("redefinir-senha.html");

		Map<String, String> replacements = new HashMap<>();
		replacements.put("usuario", nome);
		replacements.put("link", link);
		//replacements.put("today", String.valueOf(new Date()));

		String message = template.getTemplate(replacements);

		EmailDocument emailDocument = new EmailDocument(from, to, subject, message);
		emailDocument.setHtml(true);
		
		ms.sendHtmlMail(emailDocument);
	}
}

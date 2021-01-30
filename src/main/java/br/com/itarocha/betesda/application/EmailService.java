package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.EmailDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

	// VM Options:  -Dspring.profiles.active=local

	private final JavaMailSender mailSender;
	private final MailGunService ms;

	@Value("${application.link.reset}")
	private String resource;

	public void redefinirSenha(String emailDestinatario, String nome, String token) {
		
    	//String resource = env.getProperty("application.link.reset");
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
	
	public void send(EmailDocument eParams) {
		if (eParams.isHtml()) {
			try {
				sendHtmlMail(eParams);
			} catch (MessagingException e) {
				//logger.error("Could not send email to : {} Error = {}", eParams.getToAsList(), e.getMessage());
			}
		} else {
			sendPlainTextMail(eParams);
		}
	}

	private void sendHtmlMail(EmailDocument eParams) throws MessagingException {

		boolean isHtml = true;

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		helper.setReplyTo(eParams.getFrom());
		helper.setFrom(eParams.getFrom());
		helper.setSubject(eParams.getSubject());
		helper.setText(eParams.getMessage(), isHtml);

		if (eParams.getCc().size() > 0) {
			helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		mailSender.send(message);
	}

	private void sendPlainTextMail(EmailDocument eParams) {

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		eParams.getTo().toArray(new String[eParams.getTo().size()]);
		mailMessage.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		mailMessage.setReplyTo(eParams.getFrom());
		mailMessage.setFrom(eParams.getFrom());
		mailMessage.setSubject(eParams.getSubject());
		mailMessage.setText(eParams.getMessage());

		if (eParams.getCc().size() > 0) {
			mailMessage.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		mailSender.send(mailMessage);
	}
	
}

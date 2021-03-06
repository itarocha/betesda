package br.com.itarocha.betesda.adapter.out.email;

import br.com.itarocha.betesda.adapter.out.email.template.EmailTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final EmailSender emailSender;

	@Value("${application.link.reset}")
	private String resource;

	public void solicitarSenha(String emailDestinatario, String nome, String token) {
		
        String link = String.format("%s%s", resource, token);
		String from  = "Petra Sistemas <postmaster@petrasistemas.com>";
		String to = emailDestinatario;
		String subject = "Criação de nova senha";

		EmailTemplate template = new EmailTemplate("redefinir-senha.html");

		Map<String, String> replacements = new HashMap<>();
		replacements.put("usuario", nome);
		replacements.put("link", link);

		String message = template.getTemplate(replacements);

		EmailDocumentDto emailDocumentDto = new EmailDocumentDto(from, to, subject, message);
		emailDocumentDto.setHtml(true);

		emailSender.sendHtmlMail(emailDocumentDto);
	}
}

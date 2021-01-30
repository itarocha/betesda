package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.EmailDocument;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//TODO ISSO tem que ir para adapter.out
@Component
public class MailGunService {

	@Value("${application.domain.name}")
	private String domainName;

	@Value("${service.apikey}")
	private String apiKey;

	@Value("${service.url}")
	private String serviceUrl;


	public JsonNode sendHtmlMail(EmailDocument eParams) {
    	
        HttpResponse<JsonNode> request = null;
		try {
			request = Unirest.post(String.format("%s%s%s", serviceUrl,domainName,"/messages"))
			        .basicAuth("api", apiKey)
			        .queryString("from", eParams.getFrom())
			        //.queryString("to", eParams.getTo().toArray(new String[eParams.getTo().size()]))
			        .queryString("to", eParams.getTo())
			        //.queryString("to", "abc@gmail.com")
			        //.queryString("to", "dbccd@gmail.com")
			        //.queryString("cc", "asdwss@gmail.com")
			        //.queryString("bcc", "joe@example.com")
			        .queryString("subject", eParams.getSubject())
			        //.queryString("text", "Testando Email Mailgun - Deu certo!")
			        .queryString("html", eParams.getMessage())
			        //.field("attachment", new File("/temp/folder/test.txt"))
			        .asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
        return request.getBody();
    }
	
}

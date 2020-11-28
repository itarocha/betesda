package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.Email;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:env/mail.properties")
public class MailGunService {

	@Autowired
	private Environment env;
	
    public JsonNode sendHtmlMail(Email eParams) {
    	
    	String domainName = env.getProperty("application.domain.name");
    	String apiKey = env.getProperty("service.apikey"); 
    	String serviceUrl = env.getProperty("service.url"); 
    	
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
		
		System.out.println(eParams.getTo());
		System.out.println(request.getBody());

        return request.getBody();
    }
	
}

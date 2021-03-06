package br.com.itarocha.betesda.adapter.out.email;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailGunService implements EmailSender{

	@Value("${application.domain.name}")
	private String domainName;

	@Value("${service.apikey}")
	private String apiKey;

	@Value("${service.url}")
	private String serviceUrl;


	@Override
	public JsonNode sendHtmlMail(EmailDocumentDto eParams) {
    	
        HttpResponse<JsonNode> request = null;
		try {
			request = Unirest.post(String.format("%s%s%s", serviceUrl,domainName,"/messages"))
			        .basicAuth("api", apiKey)
			        .queryString("from", eParams.getFrom())
			        .queryString("to", eParams.getTo())
			        .queryString("subject", eParams.getSubject())
			        .queryString("html", eParams.getMessage())
			        .asJson();
		} catch (UnirestException e) {
			e.printStackTrace();
		}
        return request.getBody();
    }
}

package br.com.itarocha.betesda.adapter.out.email;

import kong.unirest.JsonNode;

public interface EmailSender {
    JsonNode sendHtmlMail(EmailDocumentDto document);
}

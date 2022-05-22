package br.com.itarocha.betesda.core.exceptions;

public class IntegridadeException extends RuntimeException {

    private String detailedMessage;

    public IntegridadeException(String message) {
        super(message);
    }

    public IntegridadeException(String message, String detailedMessage) {
        super(message);
        this.detailedMessage = detailedMessage;
    }

    public String getDetailedMessage(){
        return this.detailedMessage;
    }
}

package br.com.itarocha.betesda.adapter.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String detailedMessage;

    public ApiError(){
        this.setTimestamp(LocalDateTime.now());
    }

    public ApiError(String message){
        this();
        this.message = message;
    }

    public ApiError(String message, String detailedMessage){
        this(message);
        this.detailedMessage = detailedMessage;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDetailedMessage() {
        return this.detailedMessage;
    }
}


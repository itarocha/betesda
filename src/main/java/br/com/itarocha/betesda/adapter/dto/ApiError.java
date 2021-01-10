package br.com.itarocha.betesda.adapter.dto;

import br.com.itarocha.betesda.util.validation.FieldValidationError;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private List<FieldValidationError> validationErrors = new ArrayList<>();

    public ApiError(){
        this.setTimestamp(LocalDateTime.now());
        this.validationErrors = new ArrayList<>();
    }

    public ApiError(String message){
        this();
        this.setMessage(message);
    }

    public ApiError(List<FieldValidationError> validationErrors){
        this();
        this.setValidationErrors(validationErrors);
    }

    public ApiError(String message, List<FieldValidationError> validationErrors){
        this(message);
        this.setValidationErrors(validationErrors);
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

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<FieldValidationError> validationErrors) {
        if (!validationErrors.isEmpty()) {
            this.message = "Existem erros de validação";
        }
        this.validationErrors = validationErrors;
    }

}


package br.com.itarocha.betesda.exception.handler;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.exception.ResourceNotFoundException;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFoundException(ResourceNotFoundException rfnException){
        ExceptionDetails details = ExceptionDetails.builder()
                .title("Resource not found")
                .detail(rfnException.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityValidationException.class)
    public ResponseEntity<ErrorResult> handle(EntityValidationException e){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResult.builder().errors(e.getErrors()).build());
    }

    @ExceptionHandler(IntegridadeException.class)
    public ResponseEntity<ApiError> handle(IntegridadeException e){
        return new ResponseEntity(new ApiError( e.getMessage(), e.getDetailedMessage())
                                                , HttpStatus.UNPROCESSABLE_ENTITY);
    }
}

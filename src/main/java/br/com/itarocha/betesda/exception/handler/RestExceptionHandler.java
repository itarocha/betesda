package br.com.itarocha.betesda.exception.handler;

import br.com.itarocha.betesda.exception.ResourceNotFoundException;
import br.com.itarocha.betesda.util.validacoes.ErrorResult;
import br.com.itarocha.betesda.util.validacoes.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException){
        ExceptionDetails details = ExceptionDetails.builder()
                .title("Resource not found")
                .detail(rfnException.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();

        return new ResponseEntity<>(details, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handle(ValidationException e){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResult.builder().errors(e.getErrors()).build());
    }
}

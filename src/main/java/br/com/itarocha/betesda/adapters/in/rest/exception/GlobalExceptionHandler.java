package br.com.itarocha.betesda.adapters.in.rest.exception;

import br.com.itarocha.betesda.core.validation.FieldValidationError;
import br.com.itarocha.betesda.core.validation.ResultError;
import br.com.itarocha.betesda.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResultError> handleValidationException(MethodArgumentNotValidException ex) {
        ResultError resultError = new ResultError();
        
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            resultError.addError(error.getField(), error.getDefaultMessage())
        );
        
        return new ResponseEntity<>(resultError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResultError> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(ex.getRe(), HttpStatus.BAD_REQUEST);
    }
}

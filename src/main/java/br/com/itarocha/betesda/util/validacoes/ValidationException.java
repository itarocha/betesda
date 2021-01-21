package br.com.itarocha.betesda.util.validacoes;

import java.util.Set;

public class ValidationException extends RuntimeException{

    private ErrorResult errorResult;

    public ValidationException(Set<Violation> errors){
        super();
        this.errorResult = new ErrorResult();
        this.errorResult.setErrors(errors);
    }

    public ErrorResult getErrorResult(){
        return this.errorResult;
    }
}

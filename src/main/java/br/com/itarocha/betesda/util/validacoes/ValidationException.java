package br.com.itarocha.betesda.util.validacoes;

import java.util.Set;

public class ValidationException extends RuntimeException{

    private Set<Violation> errors;

    public ValidationException(Set<Violation> errors){
        super();
        this.errors = errors;
    }

    public Set<Violation> getErrors(){
        return this.errors;
    }
}

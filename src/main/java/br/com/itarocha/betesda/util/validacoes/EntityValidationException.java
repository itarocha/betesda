package br.com.itarocha.betesda.util.validacoes;

import java.io.Serializable;
import java.util.Set;

public class EntityValidationException extends RuntimeException implements Serializable {

    private Set<Violation> errors;

    public EntityValidationException(Set<Violation> errors){
        super();
        this.errors = errors;
    }

    public Set<Violation> getErrors(){
        return this.errors;
    }
}

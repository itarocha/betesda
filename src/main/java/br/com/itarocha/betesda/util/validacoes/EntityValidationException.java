package br.com.itarocha.betesda.util.validacoes;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class EntityValidationException extends RuntimeException implements Serializable {

    private Set<Violation> errors;

    public EntityValidationException(Set<Violation> errors){
        super();
        this.errors = errors;
    }

    public EntityValidationException(String fieldName, String message){
        super();
        this.errors = new HashSet<Violation>();
        this.errors.add(new Violation(fieldName, message));
    }

    public Set<Violation> getErrors(){
        return this.errors;
    }
}

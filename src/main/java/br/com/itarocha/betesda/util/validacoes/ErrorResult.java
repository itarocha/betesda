package br.com.itarocha.betesda.util.validacoes;

import lombok.Data;

import java.util.Set;

@Data
public class ErrorResult {
    private Set<Violation> errors;
}

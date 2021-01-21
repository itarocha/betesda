package br.com.itarocha.betesda.util.validacoes;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ErrorResult {
    private Set<Violation> errors;
}

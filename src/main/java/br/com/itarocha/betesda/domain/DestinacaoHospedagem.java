package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.util.validation.SelfValidating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DestinacaoHospedagem {

    private Long id;

    @NotNull(message="Descrição é obrigatória")
    @Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
    private String descricao;
}

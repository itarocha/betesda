package br.com.itarocha.betesda.adapters.in.rest.request;

import br.com.itarocha.betesda.core.domain.enums.Logico;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SituacaoLeitoRequest {
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;
	
	private Logico disponivel;
}

package br.com.itarocha.betesda.controller.request;

import br.com.itarocha.betesda.model.Logico;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TipoServicoRequest {
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;
	
	@NotNull(message="Ativo é obrigatório")
	private Logico ativo;
}

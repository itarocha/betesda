package br.com.itarocha.betesda.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeitoRequest {

	@NotNull(message="Identificador do Quarto precisa ser informado")
	private Long quartoId;
	
	@NotNull(message="Número Sequencial precisa ser informada")
	@Min(value=1, message="Número deve ser no mínimo 1" )
	private Integer numero;
	
	@NotNull(message="Tipo de Leito deve ser informado")
	private Long tipoLeitoId;
	
	@NotNull(message="Situação do Leito deve ser informada")
	private Long situacaoId;
}

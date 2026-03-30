package br.com.itarocha.betesda.core.domain.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeitoEdicao {

	private Long id;

	@NotNull(message="Identificador do Quarto precisa ser informado")
	private Long quartoId;

	private Integer quartoNumero;
	
	@NotNull(message="Número precisa ser informado")
	@Min(value=1, message="Número do Quarto deve ser maior que zero")
	private Integer numero;
	
	@NotNull(message="Tipo de Leito deve ser informada")
	private Long tipoLeito;
	
	@NotNull(message="Situação do Leito deve ser informada")
	private Long situacao;
}
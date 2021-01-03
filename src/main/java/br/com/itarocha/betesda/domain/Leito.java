package br.com.itarocha.betesda.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Leito {

	private Long id;

	@NotNull
	private Quarto quarto;

	@NotNull(message="Número Sequencial precisa ser informada")
	@Min(value=1, message="Número deve ser no mínimo 1" )
	private Integer numero;

	@NotNull(message="Tipo de Leito deve ser informado")
	private TipoLeito tipoLeito;

	@NotNull(message="Situação do Leito deve ser informada")
	private SituacaoLeito situacao;
}

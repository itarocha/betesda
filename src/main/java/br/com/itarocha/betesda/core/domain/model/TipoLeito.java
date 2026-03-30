package br.com.itarocha.betesda.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoLeito {

	private Long id;
	private String descricao;
	private Integer quantidadeCamas;
	
}

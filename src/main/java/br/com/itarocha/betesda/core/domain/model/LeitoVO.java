package br.com.itarocha.betesda.core.domain.model;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoLeitoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeitoVO {
	
	private Long id;
	
	private Integer numero;

	private Long quartoId;

	private Integer quartoNumero;
	
	private TipoLeitoEntity tipoLeito;
}
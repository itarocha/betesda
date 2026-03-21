package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.persistencia.model.TipoLeitoEntity;
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
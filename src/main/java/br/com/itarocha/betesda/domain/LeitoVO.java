package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoLeitoEntity;
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
	private TipoLeitoEntity tipoLeitoEntity;
}


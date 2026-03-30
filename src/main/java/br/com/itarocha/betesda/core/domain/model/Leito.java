package br.com.itarocha.betesda.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leito {
	private Long id;
	private Integer numero;
	private Long quartoId;
	private Integer quartoNumero;
	private TipoLeito tipoLeito;
}
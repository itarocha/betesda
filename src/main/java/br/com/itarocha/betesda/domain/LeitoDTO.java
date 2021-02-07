package br.com.itarocha.betesda.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LeitoDTO {
	private Long id;
	private Integer numero;
	private Long quartoId;
	private Integer quartoNumero;
}


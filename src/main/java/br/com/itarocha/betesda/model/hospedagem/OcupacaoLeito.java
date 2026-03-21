package br.com.itarocha.betesda.model.hospedagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OcupacaoLeito {
	
	private Long leitoId;
	
	private Boolean esta;
}
package br.com.itarocha.betesda.core.domain.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MicroLeito {
	
	private Long leitoId;
	private Integer leitoNumero;
	private Long quartoId;
	private Integer quartoNumero;
	private List<LinhaHospedagem> hospedagens = new ArrayList<>();
	
	public MicroLeito(Long leitoId, Integer leitoNumero, Long quartoId, Integer quartoNumero) {
		this.leitoId = leitoId;
		this.leitoNumero = leitoNumero;
		this.quartoId = quartoId;
		this.quartoNumero = quartoNumero;
	}
}
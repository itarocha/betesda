package br.com.itarocha.betesda.core.domain.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MiniLeito {
	
	private Long leitoId;
	private Integer leitoNumero;
	private Long quartoId;
	private Integer quartoNumero;
	private List<Linha> linhas = new ArrayList<>();
	
	public MiniLeito(Long leitoId, Integer leitoNumero, Long quartoId, Integer quartoNumero) {
		this.leitoId = leitoId;
		this.leitoNumero = leitoNumero;
		this.quartoId = quartoId;
		this.quartoNumero = quartoNumero;
	}

}

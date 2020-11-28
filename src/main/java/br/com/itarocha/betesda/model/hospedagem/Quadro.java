package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class Quadro {
	public List<QuadroQuarto> quartos = new ArrayList<>();
	
	public void setLeitoIdPorNumero(Long quartoId, Integer leitoNumero, Long leitoId) {
		this.quartos.stream()
			.filter(q -> q.id.equals(quartoId))
			.findFirst()
			.ifPresent(q -> {
				q.setLeitoIdPorNumero(leitoNumero, leitoId);
			});
	}
	
}

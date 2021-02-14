package br.com.itarocha.betesda.domain.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class QuadroQuarto {
	private Long id;
	private Integer numero;
	private List<QuadroLeito> leitos;
	
	public QuadroQuarto(Long id, Integer numero) {
		this.id = id;
		this.numero = numero;
		this.leitos = new ArrayList<>();
	}

	public void setLeitoIdPorNumero(Integer numero, Long id) {
		this.leitos.stream()
			.filter(leito -> leito.getNumero().equals(numero))
			.findFirst()
			.ifPresent(leito -> {
				leito.id = id;
			});
	}

	public void marcarDias(Long leitoId, Integer indiceDiaInicial, Integer indiceDiaFinal){
		this.leitos.stream()
				.filter(leito -> leito.getId().equals(leitoId))
				.forEach(ql -> ql.marcarRangeDias(indiceDiaInicial, indiceDiaFinal));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public List<QuadroLeito> getLeitos() {
		return leitos;
	}

	public void setLeitos(List<QuadroLeito> leitos) {
		this.leitos = leitos;
	}
}

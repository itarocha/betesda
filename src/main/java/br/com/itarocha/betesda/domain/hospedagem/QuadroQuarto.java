package br.com.itarocha.betesda.domain.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class QuadroQuarto {
	public Long id;
	public Integer numero;
	
	public QuadroQuarto(Long id, Integer numero) {
		this.id = id;
		this.numero = numero;
	}
	
	public List<QuadroLeito> leitos = new ArrayList<>();
	
	public void setLeitoIdPorNumero(Integer numero, Long id) {
		this.leitos.stream()
			.filter(leito -> leito.numero.equals(numero))
			.findFirst()
			.ifPresent(leito -> {
				leito.id = id;
			});
	}
	
}

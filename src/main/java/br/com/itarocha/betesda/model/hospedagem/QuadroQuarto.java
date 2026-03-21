package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QuadroQuarto {
	public Long id;
	public Integer numero;
	public List<QuadroLeito> leitos = new ArrayList<>();
	
	public QuadroQuarto(Long id, Integer numero) {
		this.id = id;
		this.numero = numero;
	}
	
	public void setLeitoIdPorNumero(Integer numero, Long id) {
		this.leitos.stream()
			.filter(leito -> leito.numero.equals(numero))
			.findFirst()
			.ifPresent(leito -> {
				leito.id = id;
			});
	}
	
}

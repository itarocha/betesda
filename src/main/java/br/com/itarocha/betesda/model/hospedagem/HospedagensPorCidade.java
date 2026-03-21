package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospedagensPorCidade {

	private String nome;
	private List<HospedeMapa> hospedes = new ArrayList<>();
	
	public HospedagensPorCidade(String nome, List<HospedeMapa> hospedes) {
		this.nome = nome;
		this.hospedes = hospedes;
	}
	
}

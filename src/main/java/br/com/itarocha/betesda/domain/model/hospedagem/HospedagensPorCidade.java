package br.com.itarocha.betesda.domain.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class HospedagensPorCidade {

	private String nome;
	private List<HospedeMapa> hospedes = new ArrayList<>();
	
	public HospedagensPorCidade() {}
	
	public HospedagensPorCidade(String nome, List<HospedeMapa> hospedes) {
		this.nome = nome;
		this.hospedes = hospedes;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<HospedeMapa> getHospedes() {
		return hospedes;
	}

	public void setHospedes(List<HospedeMapa> hospedes) {
		this.hospedes = hospedes;
	}
	
	
}

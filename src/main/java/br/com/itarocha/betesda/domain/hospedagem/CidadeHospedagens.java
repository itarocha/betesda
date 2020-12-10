package br.com.itarocha.betesda.domain.hospedagem;

import java.util.ArrayList;
import java.util.List;

//Utilize HospedagensPorCidade
@Deprecated
public class CidadeHospedagens {

	private String nome;
	private List<String> ids = new ArrayList<>();
	
	public CidadeHospedagens() {}
	
	public CidadeHospedagens(String nome, List<String> ids) {
		this.nome = nome;
		this.ids = ids;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}
	
	
}

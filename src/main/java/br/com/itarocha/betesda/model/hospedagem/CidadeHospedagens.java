package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CidadeHospedagens {

	private String nome;
	private List<String> ids = new ArrayList<>();
	
	public CidadeHospedagens(String nome, List<String> ids) {
		this.nome = nome;
		this.ids = ids;
	}
	
}

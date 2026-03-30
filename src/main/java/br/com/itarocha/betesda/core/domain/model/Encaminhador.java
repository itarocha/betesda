package br.com.itarocha.betesda.core.domain.model;

import br.com.itarocha.betesda.core.domain.enums.Logico;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Encaminhador {
	
	private Long id;
	private Entidade entidade;
	private String nome;
	private String cargo;
	private String telefone;
	private String email;
	private Logico ativo;

	public Encaminhador() {
		this.ativo = Logico.S;
	}
	
}

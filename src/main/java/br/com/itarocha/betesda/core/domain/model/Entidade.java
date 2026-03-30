package br.com.itarocha.betesda.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Entidade {
	
	private Long id;
	private String nome;
	private String cnpj;
	private Endereco endereco;
	private String telefone;
	private String telefone2;
	private String email;
	private String observacoes;
	private List<Encaminhador> encaminhadores;

	public Entidade(){
		this.endereco = new Endereco();
	}

	@Override
    public String toString() {
        return String.format("EntidadeEncaminhadora[id=%d, nome='%s']",id, nome);
    }	

}

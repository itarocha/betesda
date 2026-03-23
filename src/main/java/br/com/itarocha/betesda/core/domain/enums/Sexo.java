package br.com.itarocha.betesda.core.domain.enums;

public enum Sexo {

	M("Masculino"),
	F("Feminino");
	
	private String descricao;
	
	Sexo(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}

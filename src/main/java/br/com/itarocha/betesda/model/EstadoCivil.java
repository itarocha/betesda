package br.com.itarocha.betesda.model;

public enum EstadoCivil {

	S("Solteiro(a)"),
	C("Casado(a)"),
	P("Separado(a)"),
	A("Amasiado(a)"),
	D("Divorciado(a)"),
	U("União Estável"),
	V("Viúvo(a)");
	
	private String descricao;
	
	EstadoCivil(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
}

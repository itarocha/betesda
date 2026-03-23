package br.com.itarocha.betesda.core.domain.enums;

public enum SituacaoHospedagem {

	A("Aberta"),
	F("Finalizada");
	
	private String descricao;
	
	SituacaoHospedagem(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}

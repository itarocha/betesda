package br.com.itarocha.betesda.core.domain.enums;

public enum TipoUtilizacaoHospedagem {

	T("Total"),
	P("Parcial");
	
	private String descricao;
	
	TipoUtilizacaoHospedagem(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
}

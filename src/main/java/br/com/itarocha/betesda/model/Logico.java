package br.com.itarocha.betesda.model;

public enum Logico {

	S("Sim"),
	N("Não");
	
	private String descricao;
	
	Logico(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static Logico get(String code) { 
        for(Logico s : values()) {
            if(s.descricao == code) return s;
        }
        return null;
    }	
}

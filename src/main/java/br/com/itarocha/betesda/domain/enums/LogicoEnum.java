package br.com.itarocha.betesda.domain.enums;

public enum LogicoEnum {

	S("Sim"),
	N("Não");
	
	private String descricao;
	
	LogicoEnum(String descricao){
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
	public static LogicoEnum get(String code) {
        for(LogicoEnum s : values()) {
            if(s.descricao == code) return s;
        }
        return null;
    }	
}

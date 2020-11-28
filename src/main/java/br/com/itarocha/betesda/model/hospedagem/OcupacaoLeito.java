package br.com.itarocha.betesda.model.hospedagem;

public class OcupacaoLeito {
	
	private Long leitoId;
	
	private Boolean esta;
	
	public OcupacaoLeito(Long leitoId, Boolean esta) {
		this.leitoId = leitoId;
		this.esta = esta;
	}

	public Long getLeitoId() {
		return leitoId;
	}

	public void setLeitoId(Long leitoId) {
		this.leitoId = leitoId;
	}

	public Boolean getEsta() {
		return esta;
	}

	public void setEsta(Boolean esta) {
		this.esta = esta;
	}

}

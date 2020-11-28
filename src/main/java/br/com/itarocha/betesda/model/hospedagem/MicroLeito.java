package br.com.itarocha.betesda.model.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class MicroLeito {
	
	private Long leitoId;
	private Integer leitoNumero;
	private Long quartoId;
	private Integer quartoNumero;
	private List<LinhaHospedagem> hospedagens = new ArrayList<>();
	
	public MicroLeito() {
		
	}

	public MicroLeito(Long leitoId, Integer leitoNumero, Long quartoId, Integer quartoNumero) {
		this.leitoId = leitoId;
		this.leitoNumero = leitoNumero;
		this.quartoId = quartoId;
		this.quartoNumero = quartoNumero;
	}

	public Long getLeitoId() {
		return leitoId;
	}

	public void setLeitoId(Long leitoId) {
		this.leitoId = leitoId;
	}

	public Integer getLeitoNumero() {
		return leitoNumero;
	}

	public void setLeitoNumero(Integer leitoNumero) {
		this.leitoNumero = leitoNumero;
	}

	public Long getQuartoId() {
		return quartoId;
	}

	public void setQuartoId(Long quartoId) {
		this.quartoId = quartoId;
	}

	public Integer getQuartoNumero() {
		return quartoNumero;
	}

	public void setQuartoNumero(Integer quartoNumero) {
		this.quartoNumero = quartoNumero;
	}

	public List<LinhaHospedagem> getHospedagens() {
		return hospedagens;
	}

	public void setHospedagens(List<LinhaHospedagem> hospedagens) {
		this.hospedagens = hospedagens;
	}

}

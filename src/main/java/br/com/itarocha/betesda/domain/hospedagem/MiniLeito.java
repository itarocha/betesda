package br.com.itarocha.betesda.domain.hospedagem;

import java.util.ArrayList;
import java.util.List;

public class MiniLeito {
	
	private Long leitoId;
	private Integer leitoNumero;
	private Long quartoId;
	private Integer quartoNumero;
	private List<Linha> linhas = new ArrayList<>();
	
	public MiniLeito() {
		
	}

	public MiniLeito(Long leitoId, Integer leitoNumero, Long quartoId, Integer quartoNumero) {
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

	public List<Linha> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<Linha> linhas) {
		this.linhas = linhas;
	}

}

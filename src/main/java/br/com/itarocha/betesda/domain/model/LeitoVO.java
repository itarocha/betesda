package br.com.itarocha.betesda.domain.model;

import br.com.itarocha.betesda.adapter.out.persistence.entity.TipoLeitoEntity;

public class LeitoVO {
	
	private Long id;
	
	private Integer numero;

	private Long quartoId;

	private Integer quartoNumero;
	
	private TipoLeitoEntity tipoLeitoEntity;
	
	public LeitoVO() {
		
	}
	
	public LeitoVO(Long id, Integer numero, Long quartoId, Integer quartoNumero, TipoLeitoEntity tipoLeitoEntity) {
		this.id = id;
		this.numero = numero;
		this.quartoId = quartoId;
		this.quartoNumero = quartoNumero;
		this.tipoLeitoEntity = tipoLeitoEntity;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
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

	public TipoLeitoEntity getTipoLeito() {
		return tipoLeitoEntity;
	}

	public void setTipoLeito(TipoLeitoEntity tipoLeitoEntity) {
		this.tipoLeitoEntity = tipoLeitoEntity;
	}

	
}


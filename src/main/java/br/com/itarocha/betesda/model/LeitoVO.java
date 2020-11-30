package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.domain.TipoLeito;

public class LeitoVO {
	
	private Long id;
	
	private Integer numero;

	private Long quartoId;

	private Integer quartoNumero;
	
	private TipoLeito tipoLeito;
	
	public LeitoVO() {
		
	}
	
	public LeitoVO(Long id, Integer numero, Long quartoId, Integer quartoNumero, TipoLeito tipoLeito) {
		this.id = id;
		this.numero = numero;
		this.quartoId = quartoId;
		this.quartoNumero = quartoNumero;
		this.tipoLeito = tipoLeito;
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

	public TipoLeito getTipoLeito() {
		return tipoLeito;
	}

	public void setTipoLeito(TipoLeito tipoLeito) {
		this.tipoLeito = tipoLeito;
	}

	
}


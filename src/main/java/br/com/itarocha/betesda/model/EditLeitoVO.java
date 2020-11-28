package br.com.itarocha.betesda.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class EditLeitoVO {

	private Long id;

	@NotNull(message="Identificador do Quarto precisa ser informado")
	private Long quartoId;

	private Integer quartoNumero;
	
	@NotNull(message="Número precisa ser informado")
	@Min(value=1, message="Número do Quarto deve ser maior que zero")
	private Integer numero;
	
	@NotNull(message="Tipo de Leito deve ser informada")
	private Long tipoLeito;
	
	@NotNull(message="Situação do Leito deve ser informada")
	private Long situacao;
	
	public EditLeitoVO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public Long getTipoLeito() {
		return tipoLeito;
	}

	public void setTipoLeito(Long tipoLeito) {
		this.tipoLeito = tipoLeito;
	}

	public Long getSituacao() {
		return situacao;
	}

	public void setSituacao(Long situacao) {
		this.situacao = situacao;
	}

}

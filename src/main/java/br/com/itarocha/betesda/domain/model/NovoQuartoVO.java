package br.com.itarocha.betesda.domain.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class NovoQuartoVO {

	@NotNull(message="Número precisa ser informado")
	@Min(value=1, message="Número do Quarto deve ser maior que zero")
	private Integer numero;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(max = 255, message="Descrição não pode ter mais que 255 caracteres")
	private String descricao;

	//@NotNull(message="Destinação da Hospedagem é obrigatória")
	//private Long destinacaoHospedagem;
	private Long[] destinacoes;
	
	@NotNull(message="Quantidade de Leitos precisa ser informada")
	@Min(value=1, message="Quantidade de Leitos deve ser maior que zero")
	private Integer quantidadeLeitos;
	
	@NotNull(message="Tipo de Leito deve ser informada")
	private Long tipoLeito;
	
	@NotNull(message="Situação do Leito deve ser informada")
	private Long situacao;
	
	public NovoQuartoVO() {
	}
	
	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/*
	public Long getDestinacaoHospedagem() {
		return this.destinacaoHospedagem;
	}

	public void setDestinacaoHospedagem(Long destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}
	*/
	
	public Integer getQuantidadeLeitos() {
		return quantidadeLeitos;
	}

	public void setQuantidadeLeitos(Integer quantidadeLeitos) {
		this.quantidadeLeitos = quantidadeLeitos;
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

	public Long[] getDestinacoes() {
		return destinacoes;
	}

	public void setDestinacoes(Long[] destinacoes) {
		this.destinacoes = destinacoes;
	}
}

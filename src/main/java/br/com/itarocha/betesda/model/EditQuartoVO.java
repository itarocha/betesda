package br.com.itarocha.betesda.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditQuartoVO {

	@NotNull(message="Id precisa ser informado")
	private Long id;
	
	@NotNull(message="Número precisa ser informado")
	@Min(value=1, message="Número do Quarto deve ser maior que zero")
	private Integer numero;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(max = 255, message="Descrição não pode ter mais que 255 caracteres")
	private String descricao;

	/*
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private Long destinacaoHospedagem;
	*/
	
	private Long[] destinacoes;
	
	public EditQuartoVO() {
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/*
	public Long getDestinacaoHospedagem() {
		return destinacaoHospedagem;
	}

	public void setDestinacaoHospedagem(Long destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}
	*/
	
	public Long[] getDestinacoes() {
		return destinacoes;
	}

	public void setDestinacoes(Long[] destinacoes) {
		this.destinacoes = destinacoes;
	}
}

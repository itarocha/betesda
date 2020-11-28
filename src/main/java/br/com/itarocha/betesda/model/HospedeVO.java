package br.com.itarocha.betesda.model;

import java.time.LocalDate;

public class HospedeVO {

	private Long id;
	private Long pessoaId;
	private String pessoaNome;
	private LocalDate pessoaDataNascimento;
	private Long tipoHospedeId;
	private String tipoHospedeDescricao;
	private AcomodacaoVO acomodacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	public String getPessoaNome() {
		return pessoaNome;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public LocalDate getPessoaDataNascimento() {
		return pessoaDataNascimento;
	}

	public void setPessoaDataNascimento(LocalDate pessoaDataNascimento) {
		this.pessoaDataNascimento = pessoaDataNascimento;
	}
	
	public Long getTipoHospedeId() {
		return tipoHospedeId;
	}

	public void setTipoHospedeId(Long tipoHospedeId) {
		this.tipoHospedeId = tipoHospedeId;
	}

	public String getTipoHospedeDescricao() {
		return tipoHospedeDescricao;
	}

	public void setTipoHospedeDescricao(String tipoHospedeDescricao) {
		this.tipoHospedeDescricao = tipoHospedeDescricao;
	}

	public AcomodacaoVO getAcomodacao() {
		return acomodacao;
	}

	public void setAcomodacao(AcomodacaoVO acomodacao) {
		this.acomodacao = acomodacao;
	}

}

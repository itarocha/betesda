package br.com.itarocha.betesda.report;

import java.math.BigInteger;

public class PessoaEncaminhamento {
	
    private BigInteger hospedagemId;
    
    private PessoaAtendida pessoa;
	
	private String tipoUtilizacao;
	private BigInteger destinacaoHospedagemId;
	private String destinacaoHospedagemDescricao;
	private BigInteger entidadeId;
	private String entidadeNome;
	private BigInteger tipoHospedeId;
	private String tipoHospedeDescricao;
	
    public PessoaEncaminhamento() {}

	public BigInteger getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(BigInteger hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public BigInteger getDestinacaoHospedagemId() {
		return destinacaoHospedagemId;
	}

	public void setDestinacaoHospedagemId(BigInteger destinacaoHospedagemId) {
		this.destinacaoHospedagemId = destinacaoHospedagemId;
	}

	public String getDestinacaoHospedagemDescricao() {
		return destinacaoHospedagemDescricao;
	}

	public void setDestinacaoHospedagemDescricao(String destinacaoHospedagemDescricao) {
		this.destinacaoHospedagemDescricao = destinacaoHospedagemDescricao;
	}

	public BigInteger getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(BigInteger entidadeId) {
		this.entidadeId = entidadeId;
	}

	public String getEntidadeNome() {
		return entidadeNome;
	}

	public void setEntidadeNome(String entidadeNome) {
		this.entidadeNome = entidadeNome;
	}

	public BigInteger getTipoHospedeId() {
		return tipoHospedeId;
	}

	public void setTipoHospedeId(BigInteger tipoHospedeId) {
		this.tipoHospedeId = tipoHospedeId;
	}

	public String getTipoHospedeDescricao() {
		return tipoHospedeDescricao;
	}

	public void setTipoHospedeDescricao(String tipoHospedeDescricao) {
		this.tipoHospedeDescricao = tipoHospedeDescricao;
	}

	public PessoaAtendida getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaAtendida pessoa) {
		this.pessoa = pessoa;
	}

}


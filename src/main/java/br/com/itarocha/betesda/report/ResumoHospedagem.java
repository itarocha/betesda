package br.com.itarocha.betesda.report;

import java.math.BigInteger;
import java.time.LocalDate;

public class ResumoHospedagem {
	
	private BigInteger hospedagemId;
	private BigInteger hospedeId;
	private BigInteger leitoId;
	private LocalDate dataIni;
	private LocalDate dataFim;
	private Integer dias;
	private String tipoUtilizacao;
	
	private PessoaAtendida pessoa;

	private BigInteger tipoHospedeId;
	private String tipoHospedeDescricao;
	private BigInteger entidadeId;
	private String entidadeNome;
	
	public ResumoHospedagem() {
	}

	public BigInteger getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(BigInteger hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public BigInteger getHospedeId() {
		return hospedeId;
	}

	public void setHospedeId(BigInteger hospedeId) {
		this.hospedeId = hospedeId;
	}

	public BigInteger getLeitoId() {
		return leitoId;
	}

	public void setLeitoId(BigInteger leitoId) {
		this.leitoId = leitoId;
	}

	public LocalDate getDataIni() {
		return dataIni;
	}

	public void setDataIni(LocalDate dataIni) {
		this.dataIni = dataIni;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public Integer getDias() {
		return dias;
	}

	public void setDias(Integer dias) {
		this.dias = dias;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
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

	public PessoaAtendida getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaAtendida pessoa) {
		this.pessoa = pessoa;
	}

	
}

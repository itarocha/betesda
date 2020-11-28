package br.com.itarocha.betesda.model;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HospedagemVO {
	
	private Long id;

	@NotNull(message="Entidade precisa ser informada")
	private Long entidadeId;
	
	private Entidade entidade;

	@NotNull(message="Encaminhador precisa ser informado")
	private Long encaminhadorId;
	
	private Encaminhador encaminhador;
	
	@NotNull(message="Data de Entrada precisa ser informada")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data Prevista de Saída precisa ser informada")
	private LocalDate dataPrevistaSaida;
	
	@NotNull(message="Destinação de Hospedagem precisa ser informada")
    private Long destinacaoHospedagemId;
	
	private String destinacaoHospedagemDescricao;
	
	@NotNull(message="Tipo de Utilização precisa ser informada")
    private String tipoUtilizacao;
	
	private String observacoes;
	
	private Long[] servicos;
	
	private List<HospedeVO> hospedes = new ArrayList<HospedeVO>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEntidadeId() {
		return entidadeId;
	}

	public void setEntidadeId(Long entidadeId) {
		this.entidadeId = entidadeId;
	}

	public Entidade getEntidade() {
		return entidade;
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	public Long getEncaminhadorId() {
		return encaminhadorId;
	}

	public void setEncaminhadorId(Long encaminhadorId) {
		this.encaminhadorId = encaminhadorId;
	}

	public Encaminhador getEncaminhador() {
		return encaminhador;
	}

	public void setEncaminhador(Encaminhador encaminhador) {
		this.encaminhador = encaminhador;
	}

	public LocalDate getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDate getDataPrevistaSaida() {
		return dataPrevistaSaida;
	}

	public void setDataPrevistaSaida(LocalDate dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}

	public Long getDestinacaoHospedagemId() {
		return destinacaoHospedagemId;
	}

	public void setDestinacaoHospedagemId(Long destinacaoHospedagemId) {
		this.destinacaoHospedagemId = destinacaoHospedagemId;
	}

	public String getDestinacaoHospedagemDescricao() {
		return destinacaoHospedagemDescricao;
	}

	public void setDestinacaoHospedagemDescricao(String destinacaoHospedagemDescricao) {
		this.destinacaoHospedagemDescricao = destinacaoHospedagemDescricao;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Long[] getServicos() {
		return servicos;
	}

	public void setServicos(Long[] servicos) {
		this.servicos = servicos;
	}

	public List<HospedeVO> getHospedes() {
		return hospedes;
	}

	public void setHospedes(List<HospedeVO> hospedes) {
		this.hospedes = hospedes;
	} 
	
}


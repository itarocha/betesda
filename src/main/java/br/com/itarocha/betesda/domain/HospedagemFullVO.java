package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.adapter.out.persistence.entity.*;
import br.com.itarocha.betesda.domain.enums.TipoUtilizacaoHospedagemEnum;
import br.com.itarocha.betesda.domain.hospedagem.CellStatusHospedagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemFullVO {

	private Long id;
	private EntidadeEntity entidadeEntity;
	private EncaminhadorEntity encaminhadorEntity;
	private DestinacaoHospedagemEntity destinacaoHospedagemEntity;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	private TipoUtilizacaoHospedagemEnum tipoUtilizacao;
	private String observacoes;
	private CellStatusHospedagem status;
	private List<HospedeEntity> hospedeEntities = new ArrayList<HospedeEntity>();
	private List<TipoServicoEntity> servicos = new ArrayList<TipoServicoEntity>();
	
	public HospedagemFullVO() {
		this.tipoUtilizacao = TipoUtilizacaoHospedagemEnum.T;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntidadeEntity getEntidade() {
		return this.entidadeEntity;
	}

	public void setEntidade(EntidadeEntity entidadeEntity) {
		this.entidadeEntity = entidadeEntity;
	}

	public EncaminhadorEntity getEncaminhador() {
		return this.encaminhadorEntity;
	}

	public void setEncaminhador(EncaminhadorEntity encaminhadorEntity) {
		this.encaminhadorEntity = encaminhadorEntity;
	}

	public LocalDate getDataEntrada() {
		return this.dataEntrada;
	}

	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDate getDataPrevistaSaida() {
		return this.dataPrevistaSaida;
	}

	public void setDataPrevistaSaida(LocalDate dataPrevistaSaida) {
		this.dataPrevistaSaida = dataPrevistaSaida;
	}

	public LocalDate getDataEfetivaSaida() {
		return dataEfetivaSaida;
	}

	public void setDataEfetivaSaida(LocalDate dataEfetivaSaida) {
		this.dataEfetivaSaida = dataEfetivaSaida;
	}

	public String getObservacoes() {
		return this.observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public DestinacaoHospedagemEntity getDestinacaoHospedagem() {
		return destinacaoHospedagemEntity;
	}

	public void setDestinacaoHospedagem(DestinacaoHospedagemEntity destinacaoHospedagemEntity) {
		this.destinacaoHospedagemEntity = destinacaoHospedagemEntity;
	}

	public TipoUtilizacaoHospedagemEnum getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(TipoUtilizacaoHospedagemEnum tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public List<HospedeEntity> getHospedes() {
		return this.hospedeEntities;
	}

	public void setHospedes(List<HospedeEntity> hospedeEntities) {
		this.hospedeEntities = hospedeEntities;
	}

	public List<TipoServicoEntity> getServicos() {
		return servicos;
	}

	public void setServicos(List<TipoServicoEntity> servicos) {
		this.servicos = servicos;
	}

	public CellStatusHospedagem getStatus() {
		return this.status;
	}
	
	public void setStatus(CellStatusHospedagem status) {
		this.status = status;
	}
}

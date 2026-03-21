package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.hospedagem.CellStatusHospedagem;
import br.com.itarocha.betesda.persistencia.model.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.persistencia.model.EncaminhadorEntity;
import br.com.itarocha.betesda.persistencia.model.EntidadeEntity;
import br.com.itarocha.betesda.persistencia.model.HospedeEntity;
import br.com.itarocha.betesda.persistencia.model.TipoServicoEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemFullVO {

	private Long id;
	private EntidadeEntity entidade;
	private EncaminhadorEntity encaminhador;
	private DestinacaoHospedagemEntity destinacaoHospedagem;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	private String observacoes;
	private CellStatusHospedagem status;
	
	private List<HospedeEntity> hospedes = new ArrayList<HospedeEntity>();
	private List<TipoServicoEntity> servicos = new ArrayList<TipoServicoEntity>();
	
	public HospedagemFullVO() {
		this.tipoUtilizacao = TipoUtilizacaoHospedagem.T;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EntidadeEntity getEntidade() {
		return this.entidade;
	}

	public void setEntidade(EntidadeEntity entidade) {
		this.entidade = entidade;
	}

	public EncaminhadorEntity getEncaminhador() {
		return this.encaminhador;
	}

	public void setEncaminhador(EncaminhadorEntity encaminhador) {
		this.encaminhador = encaminhador;
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
		return destinacaoHospedagem;
	}

	public void setDestinacaoHospedagem(DestinacaoHospedagemEntity destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}

	public TipoUtilizacaoHospedagem getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(TipoUtilizacaoHospedagem tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public List<HospedeEntity> getHospedes() {
		return this.hospedes;
	}

	public void setHospedes(List<HospedeEntity> hospedes) {
		this.hospedes = hospedes;
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

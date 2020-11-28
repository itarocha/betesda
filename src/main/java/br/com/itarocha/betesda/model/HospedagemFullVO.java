package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.hospedagem.CellStatusHospedagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemFullVO {

	private Long id;
	private Entidade entidade;
	private Encaminhador encaminhador;
	private DestinacaoHospedagem destinacaoHospedagem;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	private String observacoes;
	private CellStatusHospedagem status;
	
	private List<Hospede> hospedes = new ArrayList<Hospede>();
	private List<TipoServico> servicos = new ArrayList<TipoServico>();
	
	public HospedagemFullVO() {
		this.tipoUtilizacao = TipoUtilizacaoHospedagem.T;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Entidade getEntidade() {
		return this.entidade;
	}

	public void setEntidade(Entidade entidade) {
		this.entidade = entidade;
	}

	public Encaminhador getEncaminhador() {
		return this.encaminhador;
	}

	public void setEncaminhador(Encaminhador encaminhador) {
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

	public DestinacaoHospedagem getDestinacaoHospedagem() {
		return destinacaoHospedagem;
	}

	public void setDestinacaoHospedagem(DestinacaoHospedagem destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}

	public TipoUtilizacaoHospedagem getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(TipoUtilizacaoHospedagem tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public List<Hospede> getHospedes() {
		return this.hospedes;
	}

	public void setHospedes(List<Hospede> hospedes) {
		this.hospedes = hospedes;
	}

	public List<TipoServico> getServicos() {
		return servicos;
	}

	public void setServicos(List<TipoServico> servicos) {
		this.servicos = servicos;
	}

	public CellStatusHospedagem getStatus() {
		return this.status;
	}
	
	public void setStatus(CellStatusHospedagem status) {
		this.status = status;
	}
}

package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hospedagem")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hospedes"})
public class Hospedagem  extends UserDateAudit implements Serializable {

	private static final long serialVersionUID = 1841335162635443594L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entidade_id")
	//////////////////@NotNull(message="Entidade é obrigatório") // por enquanto não
	private Entidade entidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="encaminhador_id")
	//////////////////@NotNull(message="Encaminhador é obrigatório") // por enquanto não
	private Encaminhador encaminhador;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="destinacao_hospedagem_id")
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private DestinacaoHospedagem destinacaoHospedagem;
			
	@NotNull(message="Data de Entrada precisa ser informado")
	@Column(name = "data_entrada")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data Prevista de Saída precisa ser informada")
	@Column(name = "data_prevista_saida")
	private LocalDate dataPrevistaSaida;
	
	@Column(name = "data_efetiva_saida")
	private LocalDate dataEfetivaSaida;

	@Enumerated(EnumType.STRING)
	@Column(name="tipo_utilizacao", length=1)
	@NotNull(message="Tipo de Utilização precisa ser informada")
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;
	
	@OneToMany(mappedBy = "hospedagem",fetch=FetchType.LAZY)
	private List<Hospede> hospedes = new ArrayList<Hospede>();
	
	@OneToMany(mappedBy = "hospedagem",fetch=FetchType.LAZY)
	private List<HospedagemTipoServico> servicos = new ArrayList<HospedagemTipoServico>();
	
	public Hospedagem() {
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

	public List<HospedagemTipoServico> getServicos() {
		return servicos;
	}

	public void setServicos(List<HospedagemTipoServico> servicos) {
		this.servicos = servicos;
	}
}

package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hospedagem")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hospedes"})
public class HospedagemEntity extends UserDateAudit implements Serializable {

	private static final long serialVersionUID = 1841335162635443594L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="entidade_id")
	private EntidadeEntity entidade;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="encaminhador_id")
	private EncaminhadorEntity encaminhador;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="destinacao_hospedagem_id")
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private DestinacaoHospedagemEntity destinacaoHospedagem;
			
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
	private List<HospedeEntity> hospedes = new ArrayList<HospedeEntity>();
	
	@OneToMany(mappedBy = "hospedagem",fetch=FetchType.LAZY)
	private List<HospedagemTipoServicoEntity> servicos = new ArrayList<HospedagemTipoServicoEntity>();
	
	public HospedagemEntity() {
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

	public List<HospedagemTipoServicoEntity> getServicos() {
		return servicos;
	}

	public void setServicos(List<HospedagemTipoServicoEntity> servicos) {
		this.servicos = servicos;
	}
}

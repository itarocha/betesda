package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.model.Logico;

import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hospede")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hospedagem"})
public class HospedeEntity extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 7525841265591324037L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospedagem_id")
	@NotNull(message="Hospedagem precisa ser informado")
	private HospedagemEntity hospedagem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pessoa_id")
	@NotNull(message="Pessoa precisa ser informada")
	private PessoaEntity pessoa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_hospede_id")
	@NotNull(message="Tipo de Hóspede precisa ser informado")
	private TipoHospedeEntity tipoHospede;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Logico baixado;
	
	@OneToMany(mappedBy = "hospede",fetch=FetchType.LAZY)
	private List<HospedeLeitoEntity> leitos = new ArrayList<HospedeLeitoEntity>();
	
	public HospedeEntity() {
		this.baixado = Logico.N;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public HospedagemEntity getHospedagem() {
		return this.hospedagem;
	}

	public void setHospedagem(HospedagemEntity hospedagem) {
		this.hospedagem = hospedagem;
	}

	public PessoaEntity getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(PessoaEntity pessoa) {
		this.pessoa = pessoa;
	}

	public TipoHospedeEntity getTipoHospede() {
		return this.tipoHospede;
	}

	public void setTipoHospede(TipoHospedeEntity tipoHospede) {
		this.tipoHospede = tipoHospede;
	}

	public Logico getBaixado() {
		return baixado;
	}

	public void setBaixado(Logico baixado) {
		this.baixado = baixado;
	}

	public List<HospedeLeitoEntity> getLeitos() {
		return leitos;
	}

	public void setLeitos(List<HospedeLeitoEntity> leitos) {
		this.leitos = leitos;
	}

}

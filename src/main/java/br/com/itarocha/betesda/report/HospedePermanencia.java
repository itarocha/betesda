package br.com.itarocha.betesda.report;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.PessoaEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties({"hibernateLazyInitializer", "hospedagem", "hospede" })
public class HospedePermanencia {
	
	private Long pessoaId;
	private Long hospedagemId;
	private Long encaminhadorId;
	private LocalDate dataEntrada;
	private LocalDate dataSaida;
	private String tipoUtilizacao;
	private Integer diasPermanencia;
	private PessoaEntity pessoaEntity;
	private HospedagemEntity hospedagemEntity;
	private HospedeEntity hospedeEntity;
	
	public HospedePermanencia(
			Long pessoaId, 
			Long hospedagemId, 
			Long encaminhadorId, 
			LocalDate dataEntrada, 
			LocalDate dataSaida, 
			String tipoUtilizacao, 
			HospedagemEntity hospedagemEntity,
			HospedeEntity hospedeEntity,
			PessoaEntity pessoaEntity) {
		
		this.pessoaId = pessoaId;
		this.hospedagemId = hospedagemId;
		this.encaminhadorId = encaminhadorId;
		this.dataEntrada = dataEntrada;
		this.dataSaida = dataSaida;
		this.tipoUtilizacao = tipoUtilizacao;
		this.hospedagemEntity = hospedagemEntity;
		this.hospedeEntity = hospedeEntity;
		this.pessoaEntity = pessoaEntity;
	}

	public Long getPessoaId() {
		return pessoaId;
	}

	public void setPessoaId(Long pessoaId) {
		this.pessoaId = pessoaId;
	}

	public Long getHospedagemId() {
		return hospedagemId;
	}

	public void setHospedagemId(Long hospedagemId) {
		this.hospedagemId = hospedagemId;
	}

	public Long getEncaminhadorId() {
		return encaminhadorId;
	}

	public void setEncaminhadorId(Long encaminhadorId) {
		this.encaminhadorId = encaminhadorId;
	}

	public LocalDate getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(LocalDate dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDate getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(LocalDate dataSaida) {
		this.dataSaida = dataSaida;
	}

	public String getTipoUtilizacao() {
		return tipoUtilizacao;
	}

	public void setTipoUtilizacao(String tipoUtilizacao) {
		this.tipoUtilizacao = tipoUtilizacao;
	}

	public Integer getDiasPermanencia() {
		return diasPermanencia;
	}

	public void setDiasPermanencia(Integer diasPermanencia) {
		this.diasPermanencia = diasPermanencia;
	}

	public PessoaEntity getPessoa() {
		return pessoaEntity;
	}

	public void setPessoa(PessoaEntity pessoaEntity) {
		this.pessoaEntity = pessoaEntity;
	}

	public HospedagemEntity getHospedagem() {
		return hospedagemEntity;
	}

	public void setHospedagem(HospedagemEntity hospedagemEntity) {
		this.hospedagemEntity = hospedagemEntity;
	}

	public HospedeEntity getHospede() {
		return hospedeEntity;
	}

	public void setHospede(HospedeEntity hospedeEntity) {
		this.hospedeEntity = hospedeEntity;
	}

}

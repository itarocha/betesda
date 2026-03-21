package br.com.itarocha.betesda.report;

import br.com.itarocha.betesda.model.HospedagemEntity;
import br.com.itarocha.betesda.model.HospedeEntity;
import br.com.itarocha.betesda.model.PessoaEntity;
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
	private PessoaEntity pessoa;
	private HospedagemEntity hospedagem;
	private HospedeEntity hospede;
	
	public HospedePermanencia(
			Long pessoaId, 
			Long hospedagemId, 
			Long encaminhadorId, 
			LocalDate dataEntrada, 
			LocalDate dataSaida, 
			String tipoUtilizacao, 
			HospedagemEntity hospedagem,
			HospedeEntity hospede,
			PessoaEntity pessoa) {
		
		this.pessoaId = pessoaId;
		this.hospedagemId = hospedagemId;
		this.encaminhadorId = encaminhadorId;
		this.dataEntrada = dataEntrada;
		this.dataSaida = dataSaida;
		this.tipoUtilizacao = tipoUtilizacao;
		this.hospedagem = hospedagem;
		this.hospede = hospede;
		this.pessoa = pessoa;
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
		return pessoa;
	}

	public void setPessoa(PessoaEntity pessoa) {
		this.pessoa = pessoa;
	}

	public HospedagemEntity getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(HospedagemEntity hospedagem) {
		this.hospedagem = hospedagem;
	}

	public HospedeEntity getHospede() {
		return hospede;
	}

	public void setHospede(HospedeEntity hospede) {
		this.hospede = hospede;
	}

}

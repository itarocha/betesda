package br.com.itarocha.betesda.report;

import br.com.itarocha.betesda.model.Hospedagem;
import br.com.itarocha.betesda.model.Hospede;
import br.com.itarocha.betesda.model.Pessoa;
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
	private Pessoa pessoa;
	private Hospedagem hospedagem;
	private Hospede hospede;
	
	public HospedePermanencia(
			Long pessoaId, 
			Long hospedagemId, 
			Long encaminhadorId, 
			LocalDate dataEntrada, 
			LocalDate dataSaida, 
			String tipoUtilizacao, 
			Hospedagem hospedagem,
			Hospede hospede,
			Pessoa pessoa) {
		
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public Hospedagem getHospedagem() {
		return hospedagem;
	}

	public void setHospedagem(Hospedagem hospedagem) {
		this.hospedagem = hospedagem;
	}

	public Hospede getHospede() {
		return hospede;
	}

	public void setHospede(Hospede hospede) {
		this.hospede = hospede;
	}

}

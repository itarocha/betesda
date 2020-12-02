package br.com.itarocha.betesda.domain.model.hospedagem;

import java.time.LocalDate;

public class PessoaLeito {

	private String identificador;
	private Long quartoId;
	private Integer quartoNumero;
	private Long leitoId;
	private Integer leitoNumero;
	private LocalDate dataEntradaLeito;
	private LocalDate dataSaidaLeito;
	private LocalDate dataIniNoPeriodo;
	private LocalDate dataFimNoPeriodo;
	private Integer[] dias;
	
	public PessoaLeito() {}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Long getQuartoId() {
		return quartoId;
	}

	public void setQuartoId(Long quartoId) {
		this.quartoId = quartoId;
	}

	public Integer getQuartoNumero() {
		return quartoNumero;
	}

	public void setQuartoNumero(Integer quartoNumero) {
		this.quartoNumero = quartoNumero;
	}

	public Long getLeitoId() {
		return leitoId;
	}

	public void setLeitoId(Long leitoId) {
		this.leitoId = leitoId;
	}

	public Integer getLeitoNumero() {
		return leitoNumero;
	}

	public void setLeitoNumero(Integer leitoNumero) {
		this.leitoNumero = leitoNumero;
	}

	public LocalDate getDataEntradaLeito() {
		return dataEntradaLeito;
	}

	public void setDataEntradaLeito(LocalDate dataEntradaLeito) {
		this.dataEntradaLeito = dataEntradaLeito;
	}

	public LocalDate getDataSaidaLeito() {
		return dataSaidaLeito;
	}

	public void setDataSaidaLeito(LocalDate dataSaidaLeito) {
		this.dataSaidaLeito = dataSaidaLeito;
	}

	public LocalDate getDataIniNoPeriodo() {
		return dataIniNoPeriodo;
	}

	public void setDataIniNoPeriodo(LocalDate dataIniNoPeriodo) {
		this.dataIniNoPeriodo = dataIniNoPeriodo;
	}

	public LocalDate getDataFimNoPeriodo() {
		return dataFimNoPeriodo;
	}

	public void setDataFimNoPeriodo(LocalDate dataFimNoPeriodo) {
		this.dataFimNoPeriodo = dataFimNoPeriodo;
	}

	public Integer[] getDias() {
		return dias;
	}

	public void setDias(Integer[] dias) {
		this.dias = dias;
	}
	
}

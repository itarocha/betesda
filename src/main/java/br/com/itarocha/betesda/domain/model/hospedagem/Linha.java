package br.com.itarocha.betesda.domain.model.hospedagem;

import java.time.LocalDate;

public class Linha {
	
	private Long hpdId;
	private String identificador;
	private String nome;
	private String telefone;
	private String status;
	private LocalDate dataIni;
	private LocalDate dataFim;
	private String clsIni;
	private String clsFim;
	
	public Linha() {}
	
	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Long getHpdId() {
		return hpdId;
	}

	public void setHpdId(Long hpdId) {
		this.hpdId = hpdId;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getDataIni() {
		return dataIni;
	}

	public void setDataIni(LocalDate dataIni) {
		this.dataIni = dataIni;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public String getClsIni() {
		return clsIni;
	}
	
	public void setClsIni(String clsIni) {
		this.clsIni = clsIni;
	}
	
	public String getClsFim() {
		return clsFim;
	}
	
	public void setClsFim(String clsFim) {
		this.clsFim = clsFim;
	}
	
}

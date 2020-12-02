package br.com.itarocha.betesda.domain.hospedagem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hospedagem"}) 
public class LinhaHospedagem {
	
	private Long hpdId;
	private String identificador;
	private String nome;
	private String telefone;
	private String status;
	private Integer idxIni;
	private Integer idxFim;
	private Integer width;
	private String clsIni;
	private String clsFim;
	private Integer[] dias;
	
	public LinhaHospedagem() {
		
	}
	
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

	public Integer getIdxIni() {
		return idxIni;
	}
	
	public void setIdxIni(Integer idxIni) {
		this.idxIni = idxIni;
	}
	
	public Integer getIdxFim() {
		return idxFim;
	}
	
	public void setIdxFim(Integer idxFim) {
		this.idxFim = idxFim;
	}
	
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
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
	
	public Integer[] getDias() {
		return dias;
	}

	public void setDias(Integer[] dias) {
		this.dias = dias;
	}
}

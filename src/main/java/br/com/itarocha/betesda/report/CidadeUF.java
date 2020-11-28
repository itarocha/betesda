package br.com.itarocha.betesda.report;

import java.io.Serializable;

public class CidadeUF implements Serializable, Comparable<CidadeUF> {

	private static final long serialVersionUID = 1709392055929655000L;
	
	private String cidade;
	private String uf;
	
	public CidadeUF(String cidade, String uf) {
		this.cidade = cidade == null ?  null : cidade.trim();
		this.uf = uf == null ?  null : uf.trim();
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.cidade + " - ");
		sb.append(this.uf);
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((uf == null) ? 0 : uf.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CidadeUF other = (CidadeUF) obj;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (uf == null) {
			if (other.uf != null)
				return false;
		} else if (!uf.equals(other.uf))
			return false;
		return true;
	}

	@Override
	public int compareTo(CidadeUF o) {
		return o.toString().compareTo(this.toString());
	}
	
}

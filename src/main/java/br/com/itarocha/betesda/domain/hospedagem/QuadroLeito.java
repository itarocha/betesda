package br.com.itarocha.betesda.domain.hospedagem;


import java.util.Arrays;

public class QuadroLeito {
	public Long id;
	public Integer numero;
	private Integer[] dias;

	/*
	public QuadroLeito(Long id, Integer numero, int numeroDias) {
		this.id = id;
		this.numero = numero;
		this.dias = new Integer[numeroDias];
		Arrays.fill(dias, 0);
	}
	*/

	public QuadroLeito(Long id, Integer numero, Integer[] dias) {
		this.id = id;
		this.numero = numero;
		this.dias = dias.clone();
	}

	public Long getId(){
		return this.id;
	}

	public Integer getNumero(){
		return this.numero;
	}

	public Integer[] getDias() {
		return dias;
	}

	public void setDias(Integer[] dias) {
		this.dias = dias;
	}

	public void marcarRangeDias(Integer idxIni, Integer idxFim) {
		Arrays.fill(this.dias, idxIni, idxFim, 1);
	}
}

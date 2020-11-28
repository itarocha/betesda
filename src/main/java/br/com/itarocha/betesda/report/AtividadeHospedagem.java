package br.com.itarocha.betesda.report;

import java.util.List;

public class AtividadeHospedagem {
	
	private String titulo;
	private String labelChave;
	private List<ChaveValor> lista;
	
	public AtividadeHospedagem(String titulo, List<ChaveValor> lista) {
		this.titulo = titulo;
		this.lista = lista;
	}

	public AtividadeHospedagem(String titulo, String labelChave, List<ChaveValor> lista) {
		this(titulo, lista);
		this.labelChave = labelChave;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getLabelChave() {
		return labelChave;
	}

	public void setLabelChave(String labelChave) {
		this.labelChave = labelChave;
	}

	public List<ChaveValor> getLista() {
		return lista;
	}

	public void setLista(List<ChaveValor> lista) {
		this.lista = lista;
	}

}

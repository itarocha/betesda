package br.com.itarocha.betesda.report;

public class ChaveValor {

	private String nome;
	private Long quantidade;
	
	public ChaveValor(String nome, Long quantidade) {
		super();
		this.nome = nome;
		this.quantidade = quantidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
	
	
	
}

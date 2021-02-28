package br.com.itarocha.betesda.report;

import java.time.LocalDate;

public class PessoaAtendida {

	private Long id;
	private String nome;
	private LocalDate dataNascimento;
	private int idade;
	private String faixaEtaria;
	private String cidade;
	private String uf;
	private String cidadeOrigem;

	public PessoaAtendida() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public LocalDate getDataNascimento() {
		return dataNascimento;
	}
	
	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public int getIdade() {
		return idade;
	}
	
	public void setIdade(int idade) {
		this.idade = idade;
		
		if (this.idade < 13) {
			this.faixaEtaria = "Criança";
		} else if (this.idade >= 13 && this.idade < 19) {
			this.faixaEtaria = "Adolescente";
		} else if (this.idade >= 19 && this.idade < 60) {
			this.faixaEtaria = "Adulto";
		} else {
			this.faixaEtaria = "Idoso";
		}
	}
	
	public String getFaixaEtaria() {
		return faixaEtaria;
	}
	
	public void setFaixaEtaria(String faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
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

	public String getCidadeOrigem() {
		return cidadeOrigem;
	}

	public void setCidadeOrigem(String cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}
}

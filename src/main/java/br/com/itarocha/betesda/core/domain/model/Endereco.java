package br.com.itarocha.betesda.core.domain.model;

import br.com.itarocha.betesda.core.domain.enums.UnidadeFederacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Endereco {

	private Long id;
	private String logradouro;
	private Integer numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String cidade;
	private UnidadeFederacao uf;
	private double latitude;
	private double longitude;
	private String descricao;

	public Endereco(){
		this.cidade = "Uberlândia";
		this.uf = UnidadeFederacao.MG;
	}

	public Endereco(String logradouro, Integer numero, String complemento, String bairro, String cep, String cidade, UnidadeFederacao uf) {
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.cidade = cidade;
		this.uf = uf;
		this.latitude = 0;
		this.longitude = 0;
	}
		
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.logradouro + ", ");
		sb.append(this.numero + ", ");
		
		if ((this.complemento != null) &&  (!this.complemento.isEmpty()) ) {
			sb.append(this.complemento + ", ");
		}

		if ((this.bairro != null) &&  (!this.bairro.isEmpty()) ) {
			sb.append("BAIRRO: "+this.bairro + ", ");
		}
		
		if ((this.cep != null) &&  (!this.cep.isEmpty()) ) {
			sb.append("CEP: "+this.cep + ", ");
		}

		sb.append(this.cidade + " - ");
		sb.append(this.uf);
		
		return sb.toString();
	}

	public String semCidadeToString(){
		StringBuilder sb = new StringBuilder();
		sb.append(this.logradouro + ", ");
		sb.append(this.numero == null ? " " : this.numero + ", ");
		
		if ((this.complemento != null) &&  (!this.complemento.isEmpty()) ) {
			sb.append(this.complemento + ", ");
		}

		if ((this.bairro != null) &&  (!this.bairro.isEmpty()) ) {
			sb.append(this.bairro);
		}
		return sb.toString();
	}
	
	public String getDescricao() {
		return this.toString();
	}
}

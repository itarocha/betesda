package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.domain.enums.UnidadeFederacaoEnum;
import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="endereco")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EnderecoEntity extends UserDateAudit implements Serializable{

	private static final long serialVersionUID = -2125966634044382751L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(max = 64, message="O endereço não pode conter mais que 64 caracteres")
	@NotNull(message="Logradouro deve ser preenchido")
	private String logradouro;

	private Integer numero;
	
	@Size(max = 32, message="O complemento do endereço não pode conter mais que 32 caracteres")
	private String complemento;
	
	@Size( max = 32, message="O bairro não pode conter mais que 32 caracteres")
	private String bairro;

	@Size(max = 8, message="Cep deve ter 9 caracteres")
	//@Pattern(regexp="(?:[0-9]{5}-[0-9]{3})?")
	private String cep;
	
	@Size(max = 64, message="A cidade não pode conter mais que 64 caracteres")
	@NotNull(message="Cidade deve ser preenchida")
	private String cidade = "Uberlândia";

	@Enumerated(EnumType.STRING)
	@Column(length = 2)
	@NotNull(message="UF deve ser preenchido")
	@Builder.Default
	private UnidadeFederacaoEnum uf = UnidadeFederacaoEnum.MG;

	@Builder.Default
	private double latitude = 0;

	@Builder.Default
	private double longitude = 0;
	
	@Transient
	private String descricao;

	/*
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
		
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public UnidadeFederacao getUf() {
		return uf;
	}

	public void setUf(UnidadeFederacao uf) {
		this.uf = uf;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	*/

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
		/*
		if ((this.cep != null) &&  (!this.cep.isEmpty()) ) {
			sb.append("CEP: "+this.cep + ", ");
		}
		*/
		return sb.toString();
	}
	
	public String getDescricao() {
		return this.toString();
	}
}

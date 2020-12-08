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

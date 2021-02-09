package br.com.itarocha.betesda.adapter.out.persistence.jpa.entity;

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
	@Column(name = "id")
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
}

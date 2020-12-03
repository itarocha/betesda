package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.domain.enums.EstadoCivilEnum;
import br.com.itarocha.betesda.domain.enums.SexoEnum;
import br.com.itarocha.betesda.domain.enums.UnidadeFederacaoEnum;
import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="pessoa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PessoaEntity extends UserDateAudit implements Serializable {
	
	private static final long serialVersionUID = 615363304475476825L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;

	@NotNull(message="Data de Nascimento é obrigatório")
	private LocalDate dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private SexoEnum sexo = SexoEnum.F;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private EstadoCivilEnum estadoCivil;
	
	@Size(min = 0, max = 11, message="CPF deve ter 11 caracteres")
	private String cpf;
	
	@Size(min = 15, max = 15, message="Cartão do SUS deve ter 15 caracteres")
	private String cartaoSus;

	@Size(max = 32, message="RG deve ter até 32 caracteres")
	private String rg;
	
	@Column(length=64)
	private String naturalidadeCidade;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 2)
	private UnidadeFederacaoEnum naturalidadeUf;
	
	@Column(length=64)
	private String nacionalidade;
	
	@Column(length=64)
	private String profissao;

	@Valid
	@ManyToOne()
	@NotNull(message="Endereço deve ser preenchido")
	private EnderecoEntity endereco = new EnderecoEntity();
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Size(max = 16, message="Telefone 2 não pode ter mais que 16 caracteres")
	private String telefone2;

	@javax.validation.constraints.Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;

	@Override
    public String toString() {
        return String.format("Pessoa[id=%d, nome='%s']",id, nome);
    }	
}

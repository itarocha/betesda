package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.core.domain.enums.EstadoCivil;
import br.com.itarocha.betesda.core.domain.enums.Sexo;
import br.com.itarocha.betesda.core.domain.enums.UnidadeFederacao;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="pessoa")
@Data
@AllArgsConstructor
public class PessoaEntity extends UserDateAudit implements Serializable {
	
	private static final long serialVersionUID = 615363304475476825L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;

	@NotNull(message="Data de Nascimento é obrigatório")
	private LocalDate dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private EstadoCivil estadoCivil;
	
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
	private UnidadeFederacao naturalidadeUf;
	
	@Column(length=64)
	private String nacionalidade;
	
	@Column(length=64)
	private String profissao;

	@Valid
	@ManyToOne()
	@NotNull(message="Endereço deve ser preenchido")
	private EnderecoEntity endereco;
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Size(max = 16, message="Telefone 2 não pode ter mais que 16 caracteres")
	private String telefone2;

	@jakarta.validation.constraints.Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;

	public PessoaEntity(){
		this.endereco = new EnderecoEntity();
		this.sexo = Sexo.F;
	}
	
	@Override
    public String toString() {
        return String.format("Pessoa[id=%d, nome='%s']",id, nome);
    }	

}

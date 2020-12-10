package br.com.itarocha.betesda.adapter.out.persistence.jpa.entity;

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
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="entidade")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "encaminhadores"})
public class EntidadeEntity extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 9099025388150371771L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 3 a 64 caracteres")
	private String nome;

	@NotNull(message="CNPJ é obrigatório")
	@Size(min = 14, max = 14, message="CNPJ deve ter 14 caracteres")
	private String cnpj;

	@Builder.Default
	@Valid
	@ManyToOne()
	@NotNull(message="Endereço é obrigatório")
	private EnderecoEntity endereco = new EnderecoEntity();;
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;

	@Size(max = 16, message="Telefone2 não pode ter mais que 16 caracteres")
	private String telefone2;
	
	@Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;

	@OneToMany(mappedBy = "entidade",fetch=FetchType.LAZY)
	private List<EncaminhadorEntity> encaminhadores;

	@Override
    public String toString() {
        return String.format("EntidadeEncaminhadora[id=%d, nome='%s']",id, nome);
    }	

}

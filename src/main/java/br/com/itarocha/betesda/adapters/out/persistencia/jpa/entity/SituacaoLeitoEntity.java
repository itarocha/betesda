package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.core.domain.enums.Logico;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name="estado_leito")
@Data
@AllArgsConstructor
public class SituacaoLeitoEntity extends UserDateAudit implements Serializable{

/*
 	LIVRE("Livre"),
	OCUPADO("Ocupado"),
	RESERVADO("Reservado"),
	EM_LIMPEZA("Em Limpeza"),
	EM_MANUTENCAO("Em Manutenção");

 */
	
	private static final long serialVersionUID = -6750385228764487323L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Logico disponivel;

	public SituacaoLeitoEntity() {
		this.disponivel = Logico.S;
	}
	
}

package br.com.itarocha.betesda.adapter.out.persistence.jpa.entity;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
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
@Table(name="estado_leito")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private LogicoEnum disponivel= LogicoEnum.S;

}

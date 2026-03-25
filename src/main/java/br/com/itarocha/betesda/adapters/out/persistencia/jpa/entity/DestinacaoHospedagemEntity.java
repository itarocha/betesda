package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name="destinacao_hospedagem")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinacaoHospedagemEntity extends UserDateAudit implements Serializable {

	private static final long serialVersionUID = 397874357784755819L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;
	
}

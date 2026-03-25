package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name="tipo_hospede")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoHospedeEntity extends UserDateAudit implements Serializable{

	private static final long serialVersionUID = 3527441284759034033L;

	/*
	PACIENTE("Paciente"),
	ACOMPANHANTE("Acompanhante");	
	*/

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message="Descrição é obrigatória")
	@Size(max = 32, message="Descrição do Tipo de Hóspede não pode ter mais que 32 caracteres")
	private String descricao;
	
}

package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="tipo_hospede")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoHospede extends UserDateAudit implements Serializable{

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

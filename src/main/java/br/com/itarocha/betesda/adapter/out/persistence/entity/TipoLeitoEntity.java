package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="tipo_leito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoLeitoEntity extends UserDateAudit implements Serializable{

	private static final long serialVersionUID = -732012434360084121L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(max = 32, message="Descrição da Destinação da Hospedagem não pode ter mais que 32 caracteres")
	private String descricao;
	
	@NotNull(message="Quantidade de Camas precisa ser informada")
	@Min(value=1, message="Quantidade de Leitos deve ser no mínimo 1" )
	@Max(value=2, message="Quantidade de Leitos deve ser no máximo 2" )
	private Integer quantidadeCamas;
}
package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name="tipo_leito")
@Data
@AllArgsConstructor
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

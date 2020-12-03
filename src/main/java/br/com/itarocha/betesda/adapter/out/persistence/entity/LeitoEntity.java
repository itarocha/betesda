package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

//import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="leito")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quarto"}) // "leitos",
public class LeitoEntity extends UserDateAudit implements Serializable {
	
	private static final long serialVersionUID = 5765750404479537331L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quarto_id")
	@NotNull
	private QuartoEntity quarto;

	@NotNull(message="Número Sequencial precisa ser informada")
	@Min(value=1, message="Número deve ser no mínimo 1" )
	private Integer numero;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="tipo_leito_id")
	@NotNull(message="Tipo de Leito deve ser informado")
	private TipoLeitoEntity tipoLeito;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="situacao_leito_id")
	@NotNull(message="Situação do Leito deve ser informada")
	private SituacaoLeitoEntity situacao;
}

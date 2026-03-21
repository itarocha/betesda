package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="leito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quarto"})
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
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public QuartoEntity getQuarto() {
		return this.quarto;
	}

	public void setQuarto(QuartoEntity quarto) {
		this.quarto = quarto;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public TipoLeitoEntity getTipoLeito() {
		return this.tipoLeito;
	}

	public void setTipoLeito(TipoLeitoEntity tipoLeito) {
		this.tipoLeito = tipoLeito;
	}

	public SituacaoLeitoEntity getSituacao() {
		return this.situacao;
	}

	public void setSituacao(SituacaoLeitoEntity situacao) {
		this.situacao = situacao;
	}
	
}

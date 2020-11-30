package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.domain.TipoLeito;
import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

//import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="leito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quarto"}) // "leitos", 
public class Leito extends UserDateAudit implements Serializable {
	
	private static final long serialVersionUID = 5765750404479537331L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quarto_id")
	@NotNull
	private Quarto quarto;

	@NotNull(message="Número Sequencial precisa ser informada")
	@Min(value=1, message="Número deve ser no mínimo 1" )
	private Integer numero;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="tipo_leito_id")
	@NotNull(message="Tipo de Leito deve ser informado")
	private TipoLeito tipoLeito;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="situacao_leito_id")
	@NotNull(message="Situação do Leito deve ser informada")
	private SituacaoLeito situacao;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Quarto getQuarto() {
		return this.quarto;
	}

	public void setQuarto(Quarto quarto) {
		this.quarto = quarto;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public TipoLeito getTipoLeito() {
		return this.tipoLeito;
	}

	public void setTipoLeito(TipoLeito tipoLeito) {
		this.tipoLeito = tipoLeito;
	}

	public SituacaoLeito getSituacao() {
		return this.situacao;
	}

	public void setSituacao(SituacaoLeito situacao) {
		this.situacao = situacao;
	}
	
}

package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="destinacao_hospedagem")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DestinacaoHospedagemEntity extends UserDateAudit implements Serializable {

	private static final long serialVersionUID = 397874357784755819L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}

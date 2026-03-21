package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="tipo_servico")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TipoServicoEntity extends UserDateAudit implements Serializable {

	private static final long serialVersionUID = -7511416572575687871L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	@NotNull(message="Ativo é obrigatório")
	private Logico ativo;

	public TipoServicoEntity() {
		this.ativo = Logico.S;
	}
	
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

	public Logico getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Logico ativo) {
		this.ativo = ativo;
	}
}

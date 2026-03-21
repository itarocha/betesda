package br.com.itarocha.betesda.persistencia.model;

import br.com.itarocha.betesda.model.Logico;

import br.com.itarocha.betesda.persistencia.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name="estado_leito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class SituacaoLeitoEntity extends UserDateAudit implements Serializable{

/*
 	LIVRE("Livre"),
	OCUPADO("Ocupado"),
	RESERVADO("Reservado"),
	EM_LIMPEZA("Em Limpeza"),
	EM_MANUTENCAO("Em Manutenção");

 */
	
	private static final long serialVersionUID = -6750385228764487323L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(min = 3, max = 32, message="Descrição deve ter entre 3 e 32 caracteres")
	private String descricao;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Logico disponivel;

	public SituacaoLeitoEntity() {
		this.disponivel = Logico.S;
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

	public Logico getDisponivel() {
		return disponivel;
	}

	public void setDisponivel(Logico disponivel) {
		this.disponivel = disponivel;
	}
	
}

package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.core.domain.enums.Logico;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="quarto")
@Data
@AllArgsConstructor
public class QuartoEntity extends UserDateAudit implements Serializable{

	private static final long serialVersionUID = -6172158858365759661L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="Número precisa ser informado")
	private Integer numero;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(max = 255, message="Descrição não pode ter mais que 255 caracteres")
	private String descricao;

	/*
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="destinacao_hospedagem_id")
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private DestinacaoHospedagemEntity destinacaoHospedagem;
	*/
	
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "quarto_destinacoes",
            joinColumns = @JoinColumn(name = "quarto_id"),
            inverseJoinColumns = @JoinColumn(name = "destinacao_hospedagem_id"))
    private Set<DestinacaoHospedagemEntity> destinacoes = new HashSet<>();
	
	@OneToMany(mappedBy = "quarto",fetch=FetchType.EAGER)
	@OrderBy("numero ASC")
	private List<LeitoEntity> leitos;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Logico ativo;

	@Transient
	private String displayText; 
	
	public QuartoEntity() {
		this.ativo = Logico.S;
	}
	
	@Transient
	public String getDisplayText() {
		return "Quarto " + ((this.numero != null) ? this.numero.toString() : "???");
	}
	
	public void setDisplayText(String value) {
		
	}
	
}

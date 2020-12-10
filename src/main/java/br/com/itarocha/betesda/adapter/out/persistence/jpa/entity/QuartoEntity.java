package br.com.itarocha.betesda.adapter.out.persistence.jpa.entity;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="quarto")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
	private DestinacaoHospedagem destinacaoHospedagem;
	*/
	
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "quarto_destinacoes",
            joinColumns = @JoinColumn(name = "quarto_id"),
            inverseJoinColumns = @JoinColumn(name = "destinacao_hospedagem_id"))
    private Set<DestinacaoHospedagemEntity> destinacoes = new HashSet<>();
	
	@OneToMany(mappedBy = "quarto",fetch=FetchType.EAGER)
	@OrderBy("numero ASC")
	private List<LeitoEntity> leitos;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private LogicoEnum ativo = LogicoEnum.S;;

	@Transient
	private String displayText;

	//TODO VER setNumero

    public Set<DestinacaoHospedagemEntity> getDestinacoes() {
        return this.destinacoes;
    }

    public void setDestinacoes(Set<DestinacaoHospedagemEntity> destinacoes) {
        this.destinacoes = destinacoes;
    }
	
	
	public List<LeitoEntity> getLeitos() {
		return this.leitos;
	}

	public void setLeitos(List<LeitoEntity> leitos) {
		this.leitos = leitos;
	}

	public LogicoEnum getAtivo() {
		return this.ativo;
	}

	public void setAtivo(LogicoEnum ativo) {
		this.ativo = ativo;
	}		
	
	@Transient
	public String getDisplayText() {
		//return this.displayText;
		return "Quarto " + ((this.numero != null) ? this.numero.toString() : "???");
	}
	
	public void setDisplayText(String value) {
		
	}
	
}

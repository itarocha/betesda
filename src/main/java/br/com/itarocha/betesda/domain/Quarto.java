package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.Logico;
import br.com.itarocha.betesda.model.audit.UserDateAudit;
import lombok.Builder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="quarto")
public class Quarto  extends UserDateAudit implements Serializable{

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
    private Set<DestinacaoHospedagem> destinacoes = new HashSet<>();
	
	@OneToMany(mappedBy = "quarto",fetch=FetchType.EAGER)
	@OrderBy("numero ASC")
	private List<Leito> leitos;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private Logico ativo =Logico.S;;

	@Transient
	private String displayText; 
	
	public Quarto() {
		this.ativo = Logico.S;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumero() {
		return this.numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
		this.displayText = "Quarto " + ((this.numero != null) ? this.numero.toString() : "???");
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/*
	public DestinacaoHospedagem getDestinacaoHospedagem() {
		return this.destinacaoHospedagem;
	}

	public void setDestinacaoHospedagem(DestinacaoHospedagem destinacaoHospedagem) {
		this.destinacaoHospedagem = destinacaoHospedagem;
	}
	*/
	
    public Set<DestinacaoHospedagem> getDestinacoes() {
        return this.destinacoes;
    }

    public void setDestinacoes(Set<DestinacaoHospedagem> destinacoes) {
        this.destinacoes = destinacoes;
    }
	
	
	public List<Leito> getLeitos() {
		return this.leitos;
	}

	public void setLeitos(List<Leito> leitos) {
		this.leitos = leitos;
	}

	public Logico getAtivo() {
		return this.ativo;
	}

	public void setAtivo(Logico ativo) {
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

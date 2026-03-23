package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.core.domain.enums.Logico;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.audit.UserDateAudit;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hospede")
@Data
@AllArgsConstructor
public class HospedeEntity extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 7525841265591324037L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospedagem_id")
	@NotNull(message="Hospedagem precisa ser informado")
	private HospedagemEntity hospedagem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pessoa_id")
	@NotNull(message="Pessoa precisa ser informada")
	private PessoaEntity pessoa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_hospede_id")
	@NotNull(message="Tipo de Hóspede precisa ser informado")
	private TipoHospedeEntity tipoHospede;
	
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Logico baixado;
	
	@OneToMany(mappedBy = "hospede",fetch=FetchType.LAZY)
	private List<HospedeLeitoEntity> leitos = new ArrayList<HospedeLeitoEntity>();
	
	public HospedeEntity() {
		this.baixado = Logico.N;
	}
	
}

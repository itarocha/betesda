package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="hospede")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hospedagem"}) // "leitos",
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

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private LogicoEnum baixado = LogicoEnum.N;;

	@Builder.Default
	@OneToMany(mappedBy = "hospede",fetch=FetchType.LAZY)
	private List<HospedeLeitoEntity> leitos = new ArrayList<HospedeLeitoEntity>();
}

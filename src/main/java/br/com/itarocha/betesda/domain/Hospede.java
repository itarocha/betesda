package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.model.Logico;
import br.com.itarocha.betesda.model.audit.UserDateAudit;
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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="hospede")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hospedagem"}) // "leitos", 
public class Hospede extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 7525841265591324037L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospedagem_id")
	@NotNull(message="Hospedagem precisa ser informado")
	private Hospedagem hospedagem;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pessoa_id")
	@NotNull(message="Pessoa precisa ser informada")
	private Pessoa pessoa;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="tipo_hospede_id")
	@NotNull(message="Tipo de Hóspede precisa ser informado")
	private TipoHospede tipoHospede;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private Logico baixado = Logico.N;;

	@Builder.Default
	@OneToMany(mappedBy = "hospede",fetch=FetchType.LAZY)
	private List<HospedeLeito> leitos = new ArrayList<HospedeLeito>();
}

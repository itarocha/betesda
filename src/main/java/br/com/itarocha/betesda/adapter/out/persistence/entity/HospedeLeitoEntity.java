package br.com.itarocha.betesda.adapter.out.persistence.entity;

import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="hospede_leito")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quarto", "leito", "hospede"})
public class HospedeLeitoEntity extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 5590030840087022870L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospede_id")
	@NotNull(message="Hóspede precisa ser informado")
	private HospedeEntity hospede;

	@NotNull(message="Data de Entrada precisa ser informado")
	@Column(name="data_entrada")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data de Saída precisa ser informado")
	@Column(name="data_saida")
	private LocalDate dataSaida;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quarto_id")
	@NotNull(message="Quarto precisa ser informado")
	private QuartoEntity quarto;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="leito_id")
	@NotNull(message="Leito precisa ser informado")
	private LeitoEntity leito;
	
	@Transient
	private Integer quartoNumero;
	
	@Transient
	private Integer leitoNumero;
}

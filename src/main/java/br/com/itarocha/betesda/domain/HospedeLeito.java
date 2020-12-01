package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="hospede_leito")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "quarto", "leito", "hospede"})
public class HospedeLeito extends UserDateAudit implements Serializable{
	
	private static final long serialVersionUID = 5590030840087022870L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="hospede_id")
	@NotNull(message="Hóspede precisa ser informado")
	private Hospede hospede;

	@NotNull(message="Data de Entrada precisa ser informado")
	@Column(name="data_entrada")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data de Saída precisa ser informado")
	@Column(name="data_saida")
	private LocalDate dataSaida;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="quarto_id")
	@NotNull(message="Quarto precisa ser informado")
	private Quarto quarto;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="leito_id")
	@NotNull(message="Leito precisa ser informado")
	private Leito leito;
	
	@Transient
	private Integer quartoNumero;
	
	@Transient
	private Integer leitoNumero;
}

package br.com.itarocha.betesda.persistencia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.util.Date;

@Entity(name="planilha_estadia")
@Data
@AllArgsConstructor
public class PlanilhaEstadiaEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length=16)
	private String codigo;

	@Column(length=16)
	private String cpf;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dataEntrada;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dataSaida;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dataExpectativaSaida;
	
	@Lob 
	@Basic(fetch=FetchType.LAZY)
	private String observacoes;
	
}

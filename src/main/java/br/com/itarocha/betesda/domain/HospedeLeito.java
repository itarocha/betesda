package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.adapter.out.persistence.audit.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HospedeLeito extends UserDateAudit implements Serializable{
	
	private Long id;
	
	@NotNull(message="Hóspede precisa ser informado")
	private Hospede hospede;

	@NotNull(message="Data de Entrada precisa ser informado")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data de Saída precisa ser informado")
	private LocalDate dataSaida;
	
	@NotNull(message="Quarto precisa ser informado")
	private Quarto quarto;
	
	@NotNull(message="Leito precisa ser informado")
	private Leito leito;
}

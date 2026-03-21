package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PessoaLeito {

	private String identificador;
	private Long quartoId;
	private Integer quartoNumero;
	private Long leitoId;
	private Integer leitoNumero;
	private LocalDate dataEntradaLeito;
	private LocalDate dataSaidaLeito;
	private LocalDate dataIniNoPeriodo;
	private LocalDate dataFimNoPeriodo;
	private Integer[] dias;
	
}

package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Linha {
	
	private Long hpdId;
	private String identificador;
	private String nome;
	private String telefone;
	private String status;
	private LocalDate dataIni;
	private LocalDate dataFim;
	private String clsIni;
	private String clsFim;
	
}

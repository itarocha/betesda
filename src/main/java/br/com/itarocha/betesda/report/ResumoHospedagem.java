package br.com.itarocha.betesda.report;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class ResumoHospedagem {
	
	private Long hospedagemId;
	private Long hospedeId;
	private Long leitoId;
	private LocalDate dataIni;
	private LocalDate dataFim;
	private Integer dias;
	private String tipoUtilizacao;
	private Long pessoaId;
	
	private PessoaAtendida pessoa;

	private Long tipoHospedeId;
	private String tipoHospedeDescricao;
	private Long entidadeId;
	private String entidadeNome;
}

package br.com.itarocha.betesda.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class HospedeNew {
	private Long id;
	private Long pessoaId;
	private String pessoaNome;
	private LocalDate pessoaDataNascimento;
	private Long tipoHospedeId;
	//private String tipoHospedeDescricao;
	private AcomodacaoNew acomodacao;
}

package br.com.itarocha.betesda.core.domain.model;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospedeVO {

	private Long id;
	private Long pessoaId;
	private String pessoaNome;
	private LocalDate pessoaDataNascimento;
	private Long tipoHospedeId;
	private String tipoHospedeDescricao;
	private AcomodacaoVO acomodacao;
}
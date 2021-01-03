package br.com.itarocha.betesda.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class QuartoEdit {

	@NotNull(message="Id precisa ser informado")
	private Long id;
	
	@NotNull(message="Número precisa ser informado")
	@Min(value=1, message="Número do Quarto deve ser maior que zero")
	private Integer numero;
	
	@NotNull(message="Descrição é obrigatória")
	@Size(max = 255, message="Descrição não pode ter mais que 255 caracteres")
	private String descricao;

	/*
	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private Long destinacaoHospedagem;
	*/
	
	private Long[] destinacoes;
}

package br.com.itarocha.betesda.controller.request;

import br.com.itarocha.betesda.model.TipoUtilizacaoHospedagem;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class HospedagemRequest {

	private Long entidadeId;
	
	private Long encaminhadorId;

	@NotNull(message="Destinação da Hospedagem é obrigatória")
	private Long destinacaoHospedagemId;
			
	@NotNull(message="Data de Entrada precisa ser informado")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data Prevista de Saída precisa ser informada")
	private LocalDate dataPrevistaSaida;
	
	private LocalDate dataEfetivaSaida;

	@NotNull(message="Tipo de Utilização precisa ser informada")
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	
	private String observacoes;
}

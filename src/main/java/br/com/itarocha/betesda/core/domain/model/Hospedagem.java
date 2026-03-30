package br.com.itarocha.betesda.core.domain.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Hospedagem {
	
	private Long id;

	@NotNull(message="Entidade precisa ser informada")
	private Long entidadeId;
	
	private Entidade entidade;

	@NotNull(message="Encaminhador precisa ser informado")
	private Long encaminhadorId;
	
	private Encaminhador encaminhador;
	
	@NotNull(message="Data de Entrada precisa ser informada")
	private LocalDate dataEntrada;
	
	@NotNull(message="Data Prevista de Saída precisa ser informada")
	private LocalDate dataPrevistaSaida;
	
	@NotNull(message="Destinação de Hospedagem precisa ser informada")
    private Long destinacaoHospedagemId;
	
	private String destinacaoHospedagemDescricao;
	
	@NotNull(message="Tipo de Utilização precisa ser informada")
    private String tipoUtilizacao;
	
	private String observacoes;
	
	private Long[] servicos;
	
	private List<Hospede> hospedes = new ArrayList<>();
}
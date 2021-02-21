package br.com.itarocha.betesda.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class HospedagemNew {
	
	private Long id;

	@NotNull(message="Entidade precisa ser informada")
	private Long entidadeId;
	
	//private Entidade entidade;

	@NotNull(message="Encaminhador precisa ser informado")
	private Long encaminhadorId;
	
	//private Encaminhador encaminhador;
	
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
	
	private List<HospedeNew> hospedes = new ArrayList<>();
}


package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.domain.enums.TipoUtilizacaoHospedagemEnum;
import br.com.itarocha.betesda.domain.hospedagem.CellStatusHospedagem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hospedagem {
	private Long id;
	private Entidade entidade;
	private Encaminhador encaminhador;
	private DestinacaoHospedagem destinacaoHospedagem;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	@Builder.Default
	private TipoUtilizacaoHospedagemEnum tipoUtilizacao = TipoUtilizacaoHospedagemEnum.T;
	private String observacoes;
	private CellStatusHospedagem status;
	private List<Hospede> hospedes = new ArrayList<>();
	private List<TipoServico> servicos = new ArrayList<>();
}

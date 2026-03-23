package br.com.itarocha.betesda.core.domain.model.hospedagem;

import java.time.LocalDate;

import br.com.itarocha.betesda.core.domain.enums.CellStatusHospedagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HospedeLeitoMapa {

	private String identificador;
	private String tipoUtilizacao;
	private String tipoUtilizacaoDescricao;
	private Long quartoId;
	private Integer quartoNumero;
	private Long leitoId;
	private Integer leitoNumero;
	private Long pessoaId;
	private String pessoaNome;
	private String pessoaTelefone;
	private String cidade;
	private String uf;
	private String pessoaCidadeUfOrigem;
	private LocalDate dataEntradaHospedagem;
	private LocalDate dataSaidaHospedagem;
	private LocalDate dataPrimeiraEntrada;
	private LocalDate dataUltimaEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	private LocalDate dataEntradaLeito;
	private LocalDate dataSaidaLeito;
	private LocalDate dataIniNoPeriodo;
	private LocalDate dataFimNoPeriodo;
	private Long hospedagemId;
	private Long hospedeId;
	private Long tipoHospedeId;
	private Boolean baixado;
	private String tipoHospedeDescricao;
	private Long destinacaoHospedagemId;
	private String	destinacaoHospedagemDescricao;
	private Integer[] dias;
	private CellStatusHospedagem statusHospedagem;
	
}

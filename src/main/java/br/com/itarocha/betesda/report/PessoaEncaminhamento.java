package br.com.itarocha.betesda.report;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PessoaEncaminhamento {
	private Long hospedagemId;
    private PessoaAtendida pessoa;
	private String tipoUtilizacao;
	private Long destinacaoHospedagemId;
	private String destinacaoHospedagemDescricao;
	private Long entidadeId;
	private String entidadeNome;
	private Long tipoHospedeId;
	private String tipoHospedeDescricao;
}


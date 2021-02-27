package br.com.itarocha.betesda.domain.hospedagem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class PessoaEncaminhada {
    private Long hospedagemId;
    private Long pessoaId;
    private String pessoaNome;
    private LocalDate pessoaDataNascimento;
    private String cidade;
    private String uf;
    private Long tipoHospedeId;
    private String tipoHospedeDescricao;
    private String tipoUtilizacao;
    private Long destinacaoHospedagemId;
    private String destinacaoHospedagemDescricao;
    private Long entidadeId;
    private String entidadeNome;
}

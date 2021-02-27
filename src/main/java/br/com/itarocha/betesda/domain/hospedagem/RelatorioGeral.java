package br.com.itarocha.betesda.domain.hospedagem;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class RelatorioGeral {
    private Long hospedagemId;
    private Long hospedeId;
    private Long leitoId;
    private LocalDate dataIni;
    private LocalDate dataFim;
    private Integer dias;
    private String tipoUtilizacao;
    private Long pessoaId;
    private String pessoaNome;
    private LocalDate pessoaDataNascimento;
    private String cidade;
    private String uf;
    private Long tipoHospedeId;
    private String tipoHospedeDescricao;
    private Long entidadeId;
    private String entidadeNome;
}

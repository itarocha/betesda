package br.com.itarocha.betesda.core.ports.out.report;

import br.com.itarocha.betesda.domain.hospedagem.PessoaEncaminhada;
import br.com.itarocha.betesda.domain.hospedagem.RelatorioGeral;

import java.time.LocalDate;
import java.util.List;

public interface RelatorioGeralRepository {

    List<PessoaEncaminhada> listPessoasEncaminhadas(LocalDate dataIni, LocalDate dataFim);
    List<RelatorioGeral> listRelatorioGeral(LocalDate dataIni, LocalDate dataFim);

}

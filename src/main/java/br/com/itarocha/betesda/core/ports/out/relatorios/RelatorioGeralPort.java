package br.com.itarocha.betesda.core.ports.out.relatorios;

import br.com.itarocha.betesda.core.domain.model.report.*;
import java.time.LocalDate;

public interface RelatorioGeralPort {

	RelatorioAtendimentos buildNovaPlanilha(LocalDate dataIni, LocalDate dataFim);
	
}

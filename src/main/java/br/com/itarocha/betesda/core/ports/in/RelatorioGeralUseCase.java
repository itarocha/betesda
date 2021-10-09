package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.report.*;
import java.time.LocalDate;

public interface RelatorioGeralUseCase {
	RelatorioAtendimentos buildPlanilha(LocalDate dataIni, LocalDate dataFim);
}
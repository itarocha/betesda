package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.report.RelatorioAtendimentos;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface PlanilhaGeralUseCase {
	ByteArrayInputStream toExcel(RelatorioAtendimentos dados)  throws IOException;
}


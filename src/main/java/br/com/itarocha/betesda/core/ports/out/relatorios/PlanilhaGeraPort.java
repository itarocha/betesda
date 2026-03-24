package br.com.itarocha.betesda.core.ports.out.relatorios;

import br.com.itarocha.betesda.core.domain.model.report.RelatorioAtendimentos;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface PlanilhaGeraPort {

	ByteArrayInputStream toExcel(RelatorioAtendimentos dados) throws IOException;

}


package br.com.itarocha.betesda.domain.hospedagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MapaQuadro {
	private LocalDate dataIni;
	private LocalDate dataFim;
	private List<LocalDate> dias = new ArrayList<>();
	private Quadro quadro;
}

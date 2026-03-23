package br.com.itarocha.betesda.core.domain.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapaCidades {
	
	private LocalDate dataIni;
	private LocalDate dataFim;
	private List<LocalDate> dias = new ArrayList<LocalDate>();
	private List<HospedagensPorCidade> cidades = new ArrayList<>();
	
}

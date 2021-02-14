package br.com.itarocha.betesda.domain.hospedagem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapaHospedes {
	private LocalDate dataIni;
	private LocalDate dataFim;
	private List<LocalDate> dias = new ArrayList<>();
	private List<HospedeMapa> hospedes = new ArrayList<>();
}

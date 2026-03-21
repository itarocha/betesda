package br.com.itarocha.betesda.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"pessoas"})
public class MapaRetorno {
	
	private LocalDate dataIni;
	private LocalDate dataFim;
	private List<LocalDate> dias = new ArrayList<LocalDate>();
	private List<CidadeHospedagens> cidades = new ArrayList<>();
	private Quadro quadro;
	private List<MicroLeito> linhas = new ArrayList<>();
	private List<MicroLeito> leitos = new ArrayList<>();
	private List<HospedeLeitoMapa> hospedes = new ArrayList<>();
	
}

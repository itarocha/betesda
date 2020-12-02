package br.com.itarocha.betesda.domain.model.hospedagem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MapaLinhas {
	
	private LocalDate dataIni;
	
	private LocalDate dataFim;
	
	private List<LocalDate> dias = new ArrayList<LocalDate>();	

	private List<MiniLeito> leitos = new ArrayList<>();
	
	public LocalDate getDataIni() {
		return dataIni;
	}

	public void setDataIni(LocalDate dataIni) {
		this.dataIni = dataIni;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public List<LocalDate> getDias() {
		return dias;
	}
	
	public void setDias(List<LocalDate> dias) {
		this.dias = dias;
	}

	public List<MiniLeito> getLeitos() {
		return leitos;
	}

	public void setLeitos(List<MiniLeito> leitos) {
		this.leitos = leitos;
	}

}

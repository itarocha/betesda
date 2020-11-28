package br.com.itarocha.betesda.model.hospedagem;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"pessoas"}) 
public class MapaRetorno {
	
	private LocalDate dataIni;
	
	private LocalDate dataFim;
	
	private List<LocalDate> dias = new ArrayList<LocalDate>();	

	private List<CidadeHospedagens> cidades = new ArrayList<>(); 
	
	
	private Quadro quadro;
	
	//TODO: Breve será chamado de leitos
	private List<MicroLeito> linhas = new ArrayList<>();

	private List<MicroLeito> leitos = new ArrayList<>();
	
	private List<HospedeLeitoMapa> hospedes = new ArrayList<>();
	
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

	public void setQuadro(Quadro quadro) {
		this.quadro = quadro;
	}
	
	public Quadro getQuadro() {
		return this.quadro;
	}

	public List<MicroLeito> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<MicroLeito> linhas) {
		this.linhas = linhas;
	}
	
	public List<MicroLeito> getLeitos() {
		return leitos;
	}

	public void setLeitos(List<MicroLeito> leitos) {
		this.leitos = leitos;
	}

	public List<HospedeLeitoMapa> getHospedes() {
		return hospedes;
	}

	public void setHospedes(List<HospedeLeitoMapa> hospedes) {
		this.hospedes = hospedes;
	}

	/*
	@Deprecated
	public List<PessoaHospedagem> getPessoas() {
		return pessoas;
	}

	@Deprecated	
	public void setPessoas(List<PessoaHospedagem> pessoas) {
		this.pessoas = pessoas;
	}
	*/
	
	public List<CidadeHospedagens> getCidades() {
		return cidades;
	}

	public void setCidades(List<CidadeHospedagens> cidades) {
		this.cidades = cidades;
	}

}

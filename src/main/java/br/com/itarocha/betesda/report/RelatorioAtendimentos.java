package br.com.itarocha.betesda.report;

import java.util.ArrayList;
import java.util.List;

public class RelatorioAtendimentos {
	
	private List<ResumoHospedagem> resumoHospedagens;
	private List<AtividadeHospedagem> atividadesHospedagem;
	private List<AtividadeHospedagem> planilhas;
	
	public RelatorioAtendimentos() {
		this.atividadesHospedagem = new ArrayList<>();
		this.planilhas = new ArrayList<>();
		this.resumoHospedagens = new ArrayList<>();
	}

	public void addAtividadeHospedagem(String titulo, List<ChaveValor> lista) {
		this.atividadesHospedagem.add(new AtividadeHospedagem(titulo, lista));
	}
	
	public List<AtividadeHospedagem> getAtividadesHospedagem() {
		return atividadesHospedagem;
	}

	public void setAtividadesHospedagem(List<AtividadeHospedagem> atividadesHospedagem) {
		this.atividadesHospedagem = atividadesHospedagem;
	}

	public void addPlanilha(String titulo, String labelChave, List<ChaveValor> lista) {
		this.planilhas.add(new AtividadeHospedagem(titulo, labelChave, lista));
	}
	
	public List<AtividadeHospedagem> getPlanilhas() {
		return planilhas;
	}

	public void setPlanilhas(List<AtividadeHospedagem> planilhas) {
		this.planilhas = planilhas;
	}

	public List<ResumoHospedagem> getResumoHospedagens() {
		return resumoHospedagens;
	}

	public void setResumoHospedagens(List<ResumoHospedagem> resumoHospedagens) {
		this.resumoHospedagens = resumoHospedagens;
	}
}

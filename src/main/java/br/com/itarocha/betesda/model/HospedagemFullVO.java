package br.com.itarocha.betesda.model;

import br.com.itarocha.betesda.model.hospedagem.CellStatusHospedagem;
import br.com.itarocha.betesda.persistencia.model.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.persistencia.model.EncaminhadorEntity;
import br.com.itarocha.betesda.persistencia.model.EntidadeEntity;
import br.com.itarocha.betesda.persistencia.model.HospedeEntity;
import br.com.itarocha.betesda.persistencia.model.TipoServicoEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemFullVO {

	private Long id;
	private EntidadeEntity entidade;
	private EncaminhadorEntity encaminhador;
	private DestinacaoHospedagemEntity destinacaoHospedagem;
	private LocalDate dataEntrada;
	private LocalDate dataPrevistaSaida;
	private LocalDate dataEfetivaSaida;
	private TipoUtilizacaoHospedagem tipoUtilizacao;
	private String observacoes;
	private CellStatusHospedagem status;
	
	private List<HospedeEntity> hospedes = new ArrayList<HospedeEntity>();
	private List<TipoServicoEntity> servicos = new ArrayList<TipoServicoEntity>();
	
	public HospedagemFullVO() {
		this.tipoUtilizacao = TipoUtilizacaoHospedagem.T;
	}
}
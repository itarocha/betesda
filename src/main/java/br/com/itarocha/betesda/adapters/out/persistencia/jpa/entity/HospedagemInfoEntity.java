package br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity;

import br.com.itarocha.betesda.core.domain.enums.TipoUtilizacaoHospedagem;
import br.com.itarocha.betesda.core.domain.enums.CellStatusHospedagem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemInfoEntity {

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
	
	public HospedagemInfoEntity() {
		this.tipoUtilizacao = TipoUtilizacaoHospedagem.T;
	}
}
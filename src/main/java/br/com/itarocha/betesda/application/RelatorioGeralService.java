package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.hospedagem.PessoaEncaminhada;
import br.com.itarocha.betesda.domain.hospedagem.RelatorioGeral;
import br.com.itarocha.betesda.report.*;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.itarocha.betesda.jooq.model.Tables.*;
import static org.jooq.impl.DSL.*;
import static org.jooq.impl.DSL.coalesce;

@RequiredArgsConstructor
@Service
public class RelatorioGeralService {

	private final DSLContext create;
	
	private List<ResumoHospedagem> listResumoHospedagem = new ArrayList<>();
	private Map<Long, PessoaAtendida> mapPessoaAtendida = new HashMap<>();


	//PlanilhaGeral
	public RelatorioAtendimentos buildNovaPlanilha(LocalDate dataIni, LocalDate dataFim) {
		
		this.listResumoHospedagem.clear();
		this.mapPessoaAtendida.clear();
		
		RelatorioAtendimentos relatorio = new RelatorioAtendimentos();

		// Quantidade de dias por tipo de utilização: tipo_utilizacao, sum(dias)
		// *** Atendimentos Realizados: Somatório de dias
		// *** Atendimentos Total/Parcial: TipoUtilização e soma de dias
		//List<ResumoHospedagem> lstResumoHospedagens = buildListaResumoHospedagem(dataIni, dataFim);
		buildListaResumoHospedagem(dataIni, dataFim);
		 
		
		Integer[] qtdAtendimentosRealizados = {0};
		Map<String, Integer> mapCidadeAtendimento = new TreeMap<>();
		Map<String, Integer> mapTipoUtilizacaoAtendimento = new HashMap<>(); // Total e Parcial
		Map<String, Integer> mapPessoasAtendimento = new HashMap<>();
		Map<String, Integer> mapEntidadeAtendimento = new TreeMap<>();

		this.listResumoHospedagem.forEach(o -> {
			qtdAtendimentosRealizados[0] += o.getDias();

			String tipoUtilizacao = "T".equals(o.getTipoUtilizacao()) ? "Total" : "Parcial";
			
			if (mapTipoUtilizacaoAtendimento.containsKey(tipoUtilizacao)) {
				Integer dias = mapTipoUtilizacaoAtendimento.get(tipoUtilizacao);
				mapTipoUtilizacaoAtendimento.put(tipoUtilizacao, o.getDias() + dias );
			} else {
				mapTipoUtilizacaoAtendimento.put(tipoUtilizacao, o.getDias());
			}

			String cidade = o.getPessoa().getCidade().trim().concat(" - ").concat(o.getPessoa().getUf());
			if (mapCidadeAtendimento.containsKey(cidade)) {
				Integer dias = mapCidadeAtendimento.get(cidade);
				mapCidadeAtendimento.put(cidade, o.getDias() + dias );
			} else {
				mapCidadeAtendimento.put(cidade, o.getDias());
			}
		
			if (mapEntidadeAtendimento.containsKey(o.getEntidadeNome())) {
				Integer dias = mapEntidadeAtendimento.get(o.getEntidadeNome());
				mapEntidadeAtendimento.put(o.getEntidadeNome(), o.getDias() + dias );
			} else {
				mapEntidadeAtendimento.put(o.getEntidadeNome(), o.getDias());
			}

		});
		mapPessoasAtendimento.put("Atendimentos Realizados", qtdAtendimentosRealizados[0]);
		
		// Quantidade de dias por tipo de utilização: tipo_utilizacao, sum(dias)
		// *** Encaminhamentos
		// *** Pessoas atendidas por faixa etária
		// *** Tipos de Hóspedes
		// *** Cidade de Origem
		//List<PessoaEncaminhamento> lstPessoasEncaminhadas = buildListaPessoaEncaminhamento(dataIni, dataFim);
		List<PessoaEncaminhamento> lstPessoasEncaminhadas = buildListaPessoaEncaminhamento(dataIni, dataFim);
		Map<String, Integer> mapEncaminhamentos = new TreeMap<>();
		Map<String, Integer> mapPessoaFaixaEtaria = new TreeMap<>();
		Map<String, Integer> mapTipoHospedeAtendimento = new TreeMap<>();
		Map<String, Integer> mapCidadeOrigem = new TreeMap<>();

		lstPessoasEncaminhadas.forEach(o -> {
			if (mapEncaminhamentos.containsKey(o.getEntidadeNome())) {
				Integer qtd = mapEncaminhamentos.get(o.getEntidadeNome());
				mapEncaminhamentos.put(o.getEntidadeNome(), ++qtd);
			} else {
				mapEncaminhamentos.put(o.getEntidadeNome(), 1);
			}

			String faixaEtaria = o.getPessoa() != null ? o.getPessoa().getFaixaEtaria() : "";
			if (mapPessoaFaixaEtaria.containsKey(faixaEtaria)) {
				Integer qtd = mapPessoaFaixaEtaria.get(faixaEtaria);
				mapPessoaFaixaEtaria.put(faixaEtaria, ++qtd);
			} else {
				mapPessoaFaixaEtaria.put(faixaEtaria, 1);
			}
			
			// Paciente, Acompanhante, Dependente...
			if (mapTipoHospedeAtendimento.containsKey(o.getTipoHospedeDescricao())) {
				Integer dias = mapTipoHospedeAtendimento.get(o.getTipoHospedeDescricao());
				mapTipoHospedeAtendimento.put(o.getTipoHospedeDescricao(), ++dias );
			} else {
				mapTipoHospedeAtendimento.put(o.getTipoHospedeDescricao(), 1);
			}
			
			String cidadeOrigem = (o.getPessoa() != null) ? o.getPessoa().getCidade().trim().concat(" - ").concat(o.getPessoa().getUf()) : "???";
			if (mapCidadeOrigem.containsKey(cidadeOrigem)) {
				Integer qtd = mapCidadeOrigem.get(cidadeOrigem);
				mapCidadeOrigem.put(cidadeOrigem, ++qtd);
			} else {
				mapCidadeOrigem.put(cidadeOrigem, 1);
			}
			
		});
		mapPessoasAtendimento.put("Pessoas Encaminhadas", lstPessoasEncaminhadas.size());
		
		relatorio.addAtividadeHospedagem("PESSOAS E ATENDIMENTOS", 			this.transforToList(mapPessoasAtendimento, true));
		relatorio.addAtividadeHospedagem("ATENDIMENTOS", 						this.transforToList(mapTipoUtilizacaoAtendimento, true));
		relatorio.addAtividadeHospedagem("TIPOS DE ATENDIMENTOS",				this.transforToList(mapTipoHospedeAtendimento, true));
		relatorio.addAtividadeHospedagem("PESSOAS ATENDIDAS POR FAIXA ETÁRIA",this.transforToList(mapPessoaFaixaEtaria, true));
		relatorio.addAtividadeHospedagem("ENCAMINHAMENTOS",					this.transforToList(mapEncaminhamentos, true));
		relatorio.addAtividadeHospedagem("CIDADE DE ORIGEM",					this.transforToList(mapCidadeOrigem, true));
		
		relatorio.setResumoHospedagens(this.listResumoHospedagem);
		
		relatorio.addPlanilha("Ranking de Encaminhadores", "Encaminhador", this.transforToList(mapEntidadeAtendimento, true));
		relatorio.addPlanilha("Ranking de Cidades", "Cidade", this.transforToList(mapCidadeAtendimento, true));
		
		return relatorio;
	}

	private List<PessoaEncaminhada> buildSelectPessoasEncaminhadas(LocalDate dataIni, LocalDate dataFim){
		br.com.itarocha.betesda.jooq.model.tables.HospedeLeito hl = HOSPEDE_LEITO.as("hl");
		Field<String> tipoUtilizacaoT = val("T");

		Condition a1 = hl.DATA_ENTRADA.between(dataIni, dataFim).or(hl.DATA_SAIDA.between(dataIni, dataFim));
		Condition a2 = hl.DATA_ENTRADA.le(dataIni).and(hl.DATA_SAIDA.ge(dataFim));
		Condition condicaoA = a1.or(a2);

		Select<Record5<Long, Long, LocalDate, LocalDate, String>> s1 =
				create.select(hl.HOSPEDE_ID
						, hl.LEITO_ID
						, when(hl.DATA_ENTRADA.lessThan(dataIni), dataIni).otherwise(hl.DATA_ENTRADA).as("data_ini")
						, when(hl.DATA_SAIDA.greaterThan(dataFim), dataFim).otherwise(hl.DATA_SAIDA).as("data_fim")
						, tipoUtilizacaoT.as("tipo_utilizacao")
				)
						.from(hl)
						.where(condicaoA);

		br.com.itarocha.betesda.jooq.model.tables.Hospedagem hpd = HOSPEDAGEM.as("hpd");
		br.com.itarocha.betesda.jooq.model.tables.Hospede h = HOSPEDE.as("h");

		Field<Long> const0 = val(0L);
		Condition b1 = hpd.DATA_ENTRADA.between(dataIni, dataFim)
				.or(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)
						.between(dataIni, dataFim));
		Condition b2 = hpd.DATA_ENTRADA.le(dataIni)
				.and(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).ge(dataFim));
		Condition b3 = hpd.TIPO_UTILIZACAO.eq("P");
		Condition condicaoB = b1.or(b2).and(b3);

		Select<Record5<Long, Long, LocalDate, LocalDate, String>> s2 =
				create.select(h.ID.as("hospede_id")
						, const0.as("leito_id")
						, when(hpd.DATA_ENTRADA.lt(dataIni), dataIni).otherwise(hpd.DATA_ENTRADA).as("data_ini")
						, when(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).greaterThan(dataFim), dataFim)
								.otherwise(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)).as("data_fim")
						, hpd.TIPO_UTILIZACAO.as("tipo_utilizacao")
				)
						.from(hpd)
						.innerJoin(h)
						.on(hpd.ID.eq(h.HOSPEDAGEM_ID))
						.where(condicaoB);

		Select<Record5<Long, Long, LocalDate, LocalDate, String>> s3 = s1.unionAll(s2);

		List<PessoaEncaminhada> lista =
				create.selectDistinct(
						  HOSPEDE.HOSPEDAGEM_ID.as("hospedagemId")
						, PESSOA.ID.as("pessoaId")
						, PESSOA.NOME.as("pessoaNome")
						, PESSOA.DATA_NASCIMENTO.as("pessoaDataNascimento")
						, ENDERECO.CIDADE
						, ENDERECO.UF
						, HOSPEDE.TIPO_HOSPEDE_ID.as("tipoHospedeId")
						, TIPO_HOSPEDE.DESCRICAO.as("tipoHospedeDescricao")
						, s3.field("tipo_utilizacao", String.class).as("tipoUtilizacao")
						, HOSPEDAGEM.DESTINACAO_HOSPEDAGEM_ID.as("destinacaoHospedagemId")
						, DESTINACAO_HOSPEDAGEM.DESCRICAO.as("destinacaoHospedagemDescricao")
						, ENTIDADE.ID.as("entidadeId")
						, ENTIDADE.NOME.as("entidadeNome")
				)
						.from(s3)
						.innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(s3.field("hospede_id", Long.class)))
						.innerJoin(HOSPEDAGEM).on(HOSPEDAGEM.ID.eq(HOSPEDE.HOSPEDAGEM_ID))
						.innerJoin(DESTINACAO_HOSPEDAGEM).on(DESTINACAO_HOSPEDAGEM.ID.eq(HOSPEDAGEM.DESTINACAO_HOSPEDAGEM_ID))
						.innerJoin(ENTIDADE).on(ENTIDADE.ID.eq(HOSPEDAGEM.ENTIDADE_ID))
						.innerJoin(TIPO_HOSPEDE).on(TIPO_HOSPEDE.ID.eq(HOSPEDE.TIPO_HOSPEDE_ID))
						.innerJoin(PESSOA).on(PESSOA.ID.eq(HOSPEDE.PESSOA_ID))
						.leftJoin(ENDERECO).on(ENDERECO.ID.eq(PESSOA.ENDERECO_ID))
						.orderBy(	PESSOA.NOME
								,   PESSOA.ID
								,	HOSPEDE.HOSPEDAGEM_ID
						)
						.fetchInto(PessoaEncaminhada.class);
		return lista;
	}

	private List<PessoaEncaminhamento> buildListaPessoaEncaminhamento(LocalDate dataIni, LocalDate dataFim) {
		// Uma pessoa pode estar presente como diversos tipos de atendimento
		// Uma pessoa pode ter sido encaminhada por várias entidades
		List<PessoaEncaminhada> listaPessoasEncaminhadas = buildSelectPessoasEncaminhadas(dataIni, dataFim);

		List<PessoaEncaminhamento> lstPessoasEncaminhadas =
		listaPessoasEncaminhadas.stream().map(record -> {
			PessoaEncaminhamento pe = new PessoaEncaminhamento();
			pe.setHospedagemId(record.getHospedagemId());
			PessoaAtendida p =  this.mapPessoaAtendida.get(record.getPessoaId());
			pe.setPessoa(p);

			pe.setTipoHospedeId(record.getTipoHospedeId());
			pe.setTipoHospedeDescricao(record.getTipoHospedeDescricao());
			pe.setTipoUtilizacao(record.getTipoUtilizacao());
			pe.setDestinacaoHospedagemId(record.getDestinacaoHospedagemId());
			pe.setDestinacaoHospedagemDescricao(record.getDestinacaoHospedagemDescricao());
			pe.setEntidadeId(record.getEntidadeId());
			pe.setEntidadeNome(record.getEntidadeNome());
			return pe;
		}).collect(Collectors.toList());

		return lstPessoasEncaminhadas;
	}

	private List<RelatorioGeral> buildSelectRelatorioGeral(LocalDate dataIni, LocalDate dataFim){
		br.com.itarocha.betesda.jooq.model.tables.HospedeLeito hl = HOSPEDE_LEITO.as("hl");
		Field<String> tipoUtilizacaoT = val("T");

		Condition a1 = hl.DATA_ENTRADA.between(dataIni, dataFim).or(hl.DATA_SAIDA.between(dataIni, dataFim));
		Condition a2 = hl.DATA_ENTRADA.le(dataIni).and(hl.DATA_SAIDA.ge(dataFim));
		Condition condicaoA = a1.or(a2);

		Select<Record5<Long, Long, LocalDate, LocalDate, String>> s1 =
			create.select(hl.HOSPEDE_ID
						, hl.LEITO_ID
						, when(hl.DATA_ENTRADA.lessThan(dataIni), dataIni).otherwise(hl.DATA_ENTRADA).as("data_ini")
						, when(hl.DATA_SAIDA.greaterThan(dataFim), dataFim).otherwise(hl.DATA_SAIDA).as("data_fim")
						, tipoUtilizacaoT.as("tipo_utilizacao")
					)
					.from(hl)
					.where(condicaoA);

		br.com.itarocha.betesda.jooq.model.tables.Hospedagem hpd = HOSPEDAGEM.as("hpd");
		br.com.itarocha.betesda.jooq.model.tables.Hospede h = HOSPEDE.as("h");

		Field<Long> const0 = val(0L);
		Condition b1 = hpd.DATA_ENTRADA.between(dataIni, dataFim)
						.or(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)
						.between(dataIni, dataFim));
		Condition b2 = hpd.DATA_ENTRADA.le(dataIni)
						.and(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).ge(dataFim));
		Condition b3 = hpd.TIPO_UTILIZACAO.eq("P");
		Condition condicaoB = b1.or(b2).and(b3);

		Select<Record5<Long, Long, LocalDate, LocalDate, String>> s2 =
				create.select(h.ID.as("hospede_id")
						, const0.as("leito_id")
						, when(hpd.DATA_ENTRADA.lt(dataIni), dataIni).otherwise(hpd.DATA_ENTRADA).as("data_ini")
						, when(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA).greaterThan(dataFim), dataFim)
								.otherwise(coalesce(hpd.DATA_EFETIVA_SAIDA, hpd.DATA_PREVISTA_SAIDA)).as("data_fim")
						, hpd.TIPO_UTILIZACAO.as("tipo_utilizacao")
				)
						.from(hpd)
						.innerJoin(h)
						.on(hpd.ID.eq(h.HOSPEDAGEM_ID))
						.where(condicaoB);

		Select<Record5<Long, Long, LocalDate, LocalDate, String>> s3 = s1.unionAll(s2);

		List<RelatorioGeral> lista =
			create.select(
					HOSPEDE.HOSPEDAGEM_ID.as("hospedagemId")
					, s3.field("hospede_id", Long.class).as("hospedeId")
					, s3.field("leito_id", Long.class).as("leitoId")
					, s3.field("data_ini", LocalDate.class).as("dataIni")
					, s3.field("data_fim", LocalDate.class).as("dataFim")
					, localDateDiff(s3.field("data_fim", LocalDate.class), s3.field("data_ini", LocalDate.class)).add(1).as("dias")
					//, DATEDIFF(tb.data_fim, tb.data_ini)+1 dias
					, s3.field("tipo_utilizacao", String.class).as("tipoUtilizacao")
					, HOSPEDE.PESSOA_ID.as("pessoaId")
					, PESSOA.NOME.as("pessoaNome")
					, PESSOA.DATA_NASCIMENTO.as("pessoaDataNascimento")
					, ENDERECO.CIDADE
					, ENDERECO.UF
					, HOSPEDE.TIPO_HOSPEDE_ID.as("tipoHospedeId")
					, TIPO_HOSPEDE.DESCRICAO.as("tipoHospedeDescricao")
					, ENTIDADE.ID.as("entidadeId")
					, ENTIDADE.NOME.as("entidadeNome")
			)
					.from(s3)
					.innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(s3.field("hospede_id", Long.class)))
					.innerJoin(PESSOA).on(PESSOA.ID.eq(HOSPEDE.PESSOA_ID))
					.innerJoin(TIPO_HOSPEDE).on(TIPO_HOSPEDE.ID.eq(HOSPEDE.TIPO_HOSPEDE_ID))
					.innerJoin(HOSPEDAGEM).on(HOSPEDAGEM.ID.eq(HOSPEDE.HOSPEDAGEM_ID))
					.innerJoin(ENTIDADE).on(ENTIDADE.ID.eq(HOSPEDAGEM.ENTIDADE_ID))
					.leftJoin(ENDERECO).on(ENDERECO.ID.eq(PESSOA.ENDERECO_ID))
					.orderBy(	HOSPEDE.ID
							, 	s3.field("data_ini") )
			.fetchInto(RelatorioGeral.class);

		return lista;
	}

	private void buildListaResumoHospedagem(LocalDate dataIni, LocalDate dataFim) {
		List<RelatorioGeral> listaRelatorioGeral = buildSelectRelatorioGeral(dataIni, dataFim);

		List<ResumoHospedagem> lista =
				listaRelatorioGeral.stream().map(r -> {
					ResumoHospedagem hr = new ResumoHospedagem();

					hr.setHospedagemId(r.getHospedagemId());
					hr.setHospedeId(r.getHospedeId());
					hr.setLeitoId(r.getLeitoId());
					hr.setDataIni(r.getDataIni());
					hr.setDataFim(r.getDataFim());
					hr.setDias(r.getDias());
					hr.setTipoUtilizacao(r.getTipoUtilizacao());

					PessoaAtendida pessoa = this.mapPessoaAtendida.get(r.getPessoaId());
					if (pessoa == null) {
						pessoa = new PessoaAtendida();
						pessoa.setId(r.getPessoaId());
						pessoa.setNome(r.getPessoaNome());
						pessoa.setDataNascimento(r.getPessoaDataNascimento());
						int idade = Period.between(r.getPessoaDataNascimento(), dataIni).getYears();
						pessoa.setIdade(idade);
						pessoa.setCidade(r.getCidade());
						pessoa.setUf(r.getUf());
						this.mapPessoaAtendida.put(r.getPessoaId(), pessoa);
					}

					hr.setPessoa(pessoa);
					hr.setTipoHospedeId(r.getTipoHospedeId());
					hr.setTipoHospedeDescricao(r.getTipoHospedeDescricao());
					hr.setEntidadeId(r.getEntidadeId());
					hr.setEntidadeNome(r.getEntidadeNome());

					return hr;
				}).collect(Collectors.toList());

		this.listResumoHospedagem = lista;
	}
	
	private List<ChaveValor> transforToList(Map<String, Integer> map, boolean byValue){
		List<ChaveValor> lst = map.entrySet().stream().map(temp -> {
			return new ChaveValor(temp.getKey(), temp.getValue() );
		}).collect(Collectors.toList());
		
		// Ordena inicialmente por chave
		lst.sort((a, b) -> a.getNome().compareTo(b.getNome()));
		
		if (byValue) {
			lst.sort((a, b) -> b.getQuantidade().compareTo(a.getQuantidade()));
		}
		
		return lst;
	}
	
}

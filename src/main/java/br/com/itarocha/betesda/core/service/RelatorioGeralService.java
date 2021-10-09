package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.out.report.RelatorioGeralRepository;
import br.com.itarocha.betesda.domain.hospedagem.PessoaEncaminhada;
import br.com.itarocha.betesda.domain.hospedagem.RelatorioGeral;
import br.com.itarocha.betesda.report.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioGeralService {


	private final RelatorioGeralRepository relatorioGeralRepository;

	private List<ResumoHospedagem> listResumoHospedagem = new ArrayList<>();
	private Map<Long, PessoaAtendida> mapPessoaAtendida = new HashMap<>();

	//PlanilhaGeral
	public RelatorioAtendimentos buildPlanilha(LocalDate dataIni, LocalDate dataFim) {
		// Quantidade de dias por tipo de utilização: tipo_utilizacao, sum(dias)
		// *** Atendimentos Realizados: Somatório de dias
		// *** Atendimentos Total/Parcial: TipoUtilização e soma de dias

		List<RelatorioGeral> listaRelatorioGeral = relatorioGeralRepository.listRelatorioGeral(dataIni, dataFim);
		this.mapPessoaAtendida = buildMapaPessoas(listaRelatorioGeral, dataIni);
		this.listResumoHospedagem = buildListaResumoHospedagem(listaRelatorioGeral, dataIni);

		Map<String, Long> mapPessoasAtendimento = new HashMap<>();

		Map<String, Long> mapEntidadeAtendimento =
				listResumoHospedagem.stream()
						.collect(Collectors.groupingBy(ResumoHospedagem::getEntidadeNome,
								Collectors.summingLong(ResumoHospedagem::getDias)  ));

		Map<String, Long> mapTipoUtilizacaoAtendimento =
				listResumoHospedagem.stream()
						.collect(Collectors.groupingBy(ResumoHospedagem::getTipoUtilizacaoDescricao ,
								Collectors.summingLong(ResumoHospedagem::getDias)  ));

		Map<String, Long> mapCidadeAtendimento =
				listResumoHospedagem.stream()
						.collect(Collectors.groupingBy(rh -> rh.getPessoa().getCidadeOrigem(),
								Collectors.summingLong(ResumoHospedagem::getDias)  ));

		Integer qtdAtendimentosRealizados = listResumoHospedagem.stream()
				.reduce(0, (parcial, rh) -> parcial + rh.getDias(), Integer::sum );

		mapPessoasAtendimento.put("Atendimentos Realizados", BigDecimal.valueOf(qtdAtendimentosRealizados).longValue());

		List<PessoaEncaminhamento> lstPessoasEncaminhadas = buildListaPessoaEncaminhamento(dataIni, dataFim);

		Map<String, Long> mapEncaminhamentos =
		lstPessoasEncaminhadas.stream()
				.collect(Collectors.groupingBy(PessoaEncaminhamento::getEntidadeNome, Collectors.counting() ));

		Map<String, Long> mapPessoaFaixaEtaria =
				lstPessoasEncaminhadas.stream()
						.collect(	Collectors.groupingBy(pe -> pe.getPessoa().getFaixaEtaria(),
									Collectors.counting() ));

		Map<String, Long> mapTipoHospedeAtendimento =
				lstPessoasEncaminhadas.stream()
				.collect(Collectors.groupingBy(PessoaEncaminhamento::getTipoHospedeDescricao,
						Collectors.counting() ));

		Map<String, Long> mapCidadeOrigem =
				lstPessoasEncaminhadas.stream()
						.collect(Collectors.groupingBy(pe -> pe.getPessoa().getCidadeOrigem(),
								LinkedHashMap::new,
								Collectors.counting() ));

		mapPessoasAtendimento.put("Pessoas Encaminhadas", BigDecimal.valueOf(lstPessoasEncaminhadas.size()).longValue() );

		RelatorioAtendimentos relatorio = new RelatorioAtendimentos();
		relatorio.addAtividadeHospedagem("PESSOAS E ATENDIMENTOS", 			this.transforToListOrderedByNome(mapPessoasAtendimento));
		relatorio.addAtividadeHospedagem("ATENDIMENTOS", 						this.transforToListOrderedByNome(mapTipoUtilizacaoAtendimento));
		relatorio.addAtividadeHospedagem("TIPOS DE ATENDIMENTOS",				this.transforToListOrderedByNome(mapTipoHospedeAtendimento));
		relatorio.addAtividadeHospedagem("PESSOAS ATENDIDAS POR FAIXA ETÁRIA",this.transforToListOrderedByValue(mapPessoaFaixaEtaria));
		relatorio.addAtividadeHospedagem("ENCAMINHAMENTOS",					this.transforToListOrderedByValue(mapEncaminhamentos));
		relatorio.addAtividadeHospedagem("CIDADE DE ORIGEM",					this.transforToListOrderedByValue(mapCidadeOrigem));
		relatorio.setResumoHospedagens(this.listResumoHospedagem);
		relatorio.addPlanilha("Ranking de Encaminhadores", "Encaminhador", this.transforToListOrderedByValue(mapEntidadeAtendimento));
		relatorio.addPlanilha("Ranking de Cidades", "Cidade", this.transforToListOrderedByValue(mapCidadeAtendimento));
		return relatorio;
	}

	private List<PessoaEncaminhamento> buildListaPessoaEncaminhamento(LocalDate dataIni, LocalDate dataFim) {
		// Uma pessoa pode estar presente como diversos tipos de atendimento
		// Uma pessoa pode ter sido encaminhada por várias entidades
		List<PessoaEncaminhada> listaPessoasEncaminhadas = relatorioGeralRepository.listPessoasEncaminhadas(dataIni, dataFim);

		List<PessoaEncaminhamento> lstPessoasEncaminhamentos = listaPessoasEncaminhadas.stream()
															.map(record -> convertEncaminhamento(record))
															.collect(Collectors.toList());

		lstPessoasEncaminhamentos.stream().forEach(pe -> pe.setPessoa(this.mapPessoaAtendida.get(pe.getPessoaId())));

		return lstPessoasEncaminhamentos;
	}

	private static PessoaEncaminhamento convertEncaminhamento(PessoaEncaminhada entrada){
		PessoaEncaminhamento pe = new PessoaEncaminhamento();
		pe.setHospedagemId(entrada.getHospedagemId());
		pe.setPessoaId(entrada.getPessoaId());
		pe.setTipoHospedeId(entrada.getTipoHospedeId());
		pe.setTipoHospedeDescricao(entrada.getTipoHospedeDescricao());
		pe.setTipoUtilizacao(entrada.getTipoUtilizacao());
		pe.setDestinacaoHospedagemId(entrada.getDestinacaoHospedagemId());
		pe.setDestinacaoHospedagemDescricao(entrada.getDestinacaoHospedagemDescricao());
		pe.setEntidadeId(entrada.getEntidadeId());
		pe.setEntidadeNome(entrada.getEntidadeNome());
		return pe;
	}

	private Map<Long, PessoaAtendida> buildMapaPessoas(List<RelatorioGeral> listaRelatorioGeral, LocalDate dataIni) {
		return listaRelatorioGeral.stream()
				.collect(Collectors.toMap(rg -> rg.getPessoaId(), rg -> convertToPessoaAtendida(rg, dataIni), (p1, p2) -> p1) );
	}

	private List<ResumoHospedagem> buildListaResumoHospedagem(List<RelatorioGeral> listaRelatorioGeral, LocalDate dataIni) {
		List<ResumoHospedagem> lista = listaRelatorioGeral.stream()
				.map(r -> convertRelatorioGeral(r, dataIni))
				.collect(Collectors.toList());

		lista.stream().forEach(rh -> rh.setPessoa(mapPessoaAtendida.get(rh.getPessoaId())));

		return lista;
	}

	private PessoaAtendida convertToPessoaAtendida(RelatorioGeral rg, LocalDate dataInicial) {
		PessoaAtendida pessoa = new PessoaAtendida();
		pessoa.setId(rg.getPessoaId());
		pessoa.setNome(rg.getPessoaNome());
		pessoa.setDataNascimento(rg.getPessoaDataNascimento());
		int idade = Period.between(rg.getPessoaDataNascimento(), dataInicial).getYears();
		pessoa.setIdade(idade);
		pessoa.setCidade(rg.getCidade());
		pessoa.setUf(rg.getUf());
		pessoa.setCidadeOrigem(rg.getCidade().trim().concat(" - ").concat(rg.getUf()));
		return pessoa;
	}

	private static ResumoHospedagem convertRelatorioGeral(RelatorioGeral model, LocalDate dataInicial/*, Map<Long, PessoaAtendida> mapPessoas*/){
		ResumoHospedagem resumo = new ResumoHospedagem();
		resumo.setHospedagemId(model.getHospedagemId());
		resumo.setHospedeId(model.getHospedeId());
		resumo.setLeitoId(model.getLeitoId());
		resumo.setDataIni(model.getDataIni());
		resumo.setDataFim(model.getDataFim());
		resumo.setDias(model.getDias());
		resumo.setTipoUtilizacao(model.getTipoUtilizacao());
		resumo.setTipoUtilizacaoDescricao("T".equals(model.getTipoUtilizacao()) ? "Total" : "Parcial");
		resumo.setTipoHospedeId(model.getTipoHospedeId());
		resumo.setTipoHospedeDescricao(model.getTipoHospedeDescricao());
		resumo.setEntidadeId(model.getEntidadeId());
		resumo.setEntidadeNome(model.getEntidadeNome());
		resumo.setPessoaId(model.getPessoaId());
		return resumo;
	}
	
	private List<ChaveValor> transforToList(Map<String, Long> map){
		List<ChaveValor> listaRetorno = map.entrySet()
				.stream()
				.map(temp -> new ChaveValor(temp.getKey(), temp.getValue() ))
				.collect(Collectors.toList());
		return listaRetorno;
	}

	private List<ChaveValor> transforToListOrderedByNome(Map<String, Long> map){
		List<ChaveValor> listaRetorno = transforToList(map);
		Collections.sort(listaRetorno, Comparator.comparing(ChaveValor::getNome));
		return listaRetorno;
	}

	private List<ChaveValor> transforToListOrderedByValue(Map<String, Long> map){
		List<ChaveValor> listaRetorno = transforToList(map);
		Collections.sort(listaRetorno, Comparator.comparing(ChaveValor::getQuantidade).reversed()
				.thenComparing(Comparator.comparing(ChaveValor::getNome))) ;
		return listaRetorno;
	}

}
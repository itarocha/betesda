package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.in.RelatorioGeralUseCase;
import br.com.itarocha.betesda.core.ports.out.report.RelatorioGeralRepository;
import br.com.itarocha.betesda.domain.hospedagem.PessoaEncaminhada;
import br.com.itarocha.betesda.domain.hospedagem.RelatorioGeral;
import br.com.itarocha.betesda.domain.mapper.PessoaEncaminhadaMapper;
import br.com.itarocha.betesda.domain.mapper.RelatorioGeralMapper;
import br.com.itarocha.betesda.report.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioGeralService implements RelatorioGeralUseCase {

	private final RelatorioGeralRepository relatorioGeralRepository;
	private final PessoaEncaminhadaMapper pessoaEncaminhadaMapper;
	private final RelatorioGeralMapper relatorioGeralMapper;

	private List<ResumoHospedagem> listResumoHospedagem = new ArrayList<>();
	private Map<Long, PessoaAtendida> mapPessoaAtendida = new HashMap<>();

	//PlanilhaGeral
	public RelatorioAtendimentos buildPlanilha(LocalDate dataIni, LocalDate dataFim) {
		// Quantidade de dias por tipo de utilização: tipo_utilizacao, sum(dias)
		// *** Atendimentos Realizados: Somatório de dias
		// *** Atendimentos Total/Parcial: TipoUtilização e soma de dias

		List<RelatorioGeral> listaRelatorioGeral = relatorioGeralRepository.listRelatorioGeral(dataIni, dataFim);
		this.mapPessoaAtendida = buildMapaPessoas(listaRelatorioGeral, dataIni);
		this.listResumoHospedagem = buildListaResumoHospedagem(listaRelatorioGeral);

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

		Map<String, Long> mapPessoaFaixaEtaria = lstPessoasEncaminhadas.stream()
				.collect(Collectors.groupingBy(pe -> pe.getPessoa().getFaixaEtaria(), Collectors.counting() ));

		Map<String, Long> mapTipoHospedeAtendimento =
				lstPessoasEncaminhadas.stream()
				.collect(Collectors.groupingBy(PessoaEncaminhamento::getTipoHospedeDescricao, Collectors.counting() ));

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
															.map(record -> pessoaEncaminhadaMapper.convertEncaminhamento(record))
															.collect(Collectors.toList());

		lstPessoasEncaminhamentos.stream().forEach(pe -> pe.setPessoa(this.mapPessoaAtendida.get(pe.getPessoaId())));

		return lstPessoasEncaminhamentos;
	}

	private Map<Long, PessoaAtendida> buildMapaPessoas(List<RelatorioGeral> listaRelatorioGeral, LocalDate dataIni) {
		return listaRelatorioGeral.stream()
				.collect(Collectors.toMap(rg -> rg.getPessoaId(), rg -> relatorioGeralMapper.convertToPessoaAtendida(rg, dataIni), (p1, p2) -> p1) );
	}

	private List<ResumoHospedagem> buildListaResumoHospedagem(List<RelatorioGeral> listaRelatorioGeral) {
		List<ResumoHospedagem> lista = listaRelatorioGeral.stream()
				.map(r -> relatorioGeralMapper.convertRelatorioGeral(r))
				.collect(Collectors.toList());

		lista.stream().forEach(rh -> rh.setPessoa(mapPessoaAtendida.get(rh.getPessoaId())));

		return lista;
	}

	private List<ChaveValor> transforToList(Map<String, Long> map){
		return map.entrySet()
				.stream()
				.map(temp -> new ChaveValor(temp.getKey(), temp.getValue() ))
				.collect(Collectors.toList());
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
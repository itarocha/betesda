package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.report.*;
import br.com.itarocha.betesda.utils.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RelatorioGeralService {

	
	@Autowired
	private EntityManager em;
	
	private List<ResumoHospedagem> listResumoHospedagem;
	private Map<BigInteger, PessoaAtendida> mapPessoaAtendida;

	public RelatorioGeralService() {
		this.listResumoHospedagem = new ArrayList<>();
		this.mapPessoaAtendida = new HashMap<>();
	}
	
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
		
		relatorio.addAtividadeHospedagem("PESSOAS E ATENDIMENTOS", 				this.transforToList(mapPessoasAtendimento, true));
		relatorio.addAtividadeHospedagem("ATENDIMENTOS", 						this.transforToList(mapTipoUtilizacaoAtendimento, true));
		relatorio.addAtividadeHospedagem("TIPOS DE ATENDIMENTOS",				this.transforToList(mapTipoHospedeAtendimento, true));
		relatorio.addAtividadeHospedagem("PESSOAS ATENDIDAS POR FAIXA ETÁRIA",	this.transforToList(mapPessoaFaixaEtaria, true));
		relatorio.addAtividadeHospedagem("ENCAMINHAMENTOS",						this.transforToList(mapEncaminhamentos, true));
		relatorio.addAtividadeHospedagem("CIDADE DE ORIGEM",					this.transforToList(mapCidadeOrigem, true));
		
		relatorio.setResumoHospedagens(this.listResumoHospedagem);
		
		relatorio.addPlanilha("Ranking de Encaminhadores", "Encaminhador", this.transforToList(mapEntidadeAtendimento, true));
		relatorio.addPlanilha("Ranking de Cidades", "Cidade", this.transforToList(mapCidadeAtendimento, true));
		
		return relatorio;
	}
	
	private List<PessoaEncaminhamento> buildListaPessoaEncaminhamento(LocalDate dataIni, LocalDate dataFim) {
		// Uma pessoa pode estar presente como diversos tipos de atendimento
		// Uma pessoa pode ter sido encaminhada por várias entidades
	
		StringBuilder sbPesEnc = StrUtil.loadFile("/sql/estatistica/pessoas_encaminhadas.sql");
		Query qPesEnc = em.createNativeQuery(sbPesEnc.toString());
		qPesEnc.setParameter("DATA_INI", dataIni);
		qPesEnc.setParameter("DATA_FIM", dataFim );
		List<Object[]> recPessoasEnc =  qPesEnc.getResultList();
		List<PessoaEncaminhamento> lstPessoasEncaminhadas = new ArrayList<>();
		
		recPessoasEnc.stream().forEach(record -> {
			PessoaEncaminhamento pe = new PessoaEncaminhamento();
			pe.setHospedagemId((BigInteger) record[0]);
			
			BigInteger pessoaId = (BigInteger) record[1];
			PessoaAtendida p =  this.mapPessoaAtendida.get(pessoaId);
			pe.setPessoa(p);
			
			pe.setTipoHospedeId((BigInteger) record[6]);
			pe.setTipoHospedeDescricao((String) record[7]);
			pe.setTipoUtilizacao((String) record[8]);
			pe.setDestinacaoHospedagemId((BigInteger) record[9]);
			pe.setDestinacaoHospedagemDescricao((String) record[10]);
			pe.setEntidadeId((BigInteger) record[11]);
			pe.setEntidadeNome((String) record[12]);
			
		    lstPessoasEncaminhadas.add(pe);
		});
		return lstPessoasEncaminhadas;
	}
	
	private void buildListaResumoHospedagem(LocalDate dataIni, LocalDate dataFim) {
		// Resumo geral. Contagem de dias e tipos de utilização (T/P)	
		StringBuilder sb = StrUtil.loadFile("/sql/estatistica/resumo_geral_hospedagens.sql");
		Query q = em.createNativeQuery(sb.toString());
		q.setParameter("DATA_INI", dataIni);
		q.setParameter("DATA_FIM", dataFim );
		List<Object[]> recResumo =  q.getResultList();
		
		recResumo.stream().forEach(record -> {
			ResumoHospedagem hr = new ResumoHospedagem();
			
			hr.setHospedagemId((BigInteger)record[0]);
			hr.setHospedeId((BigInteger)record[1]);
			hr.setLeitoId((BigInteger)record[2]);
			hr.setDataIni(LocalDate.parse((String)record[3]));
			hr.setDataFim(LocalDate.parse((String)record[4]));
			hr.setDias((Integer)record[5]);
			hr.setTipoUtilizacao((String)record[6]);

			BigInteger pessoaId = (BigInteger)record[7];
			PessoaAtendida pessoa =  this.mapPessoaAtendida.get(pessoaId);
			if (pessoa == null) {
				pessoa = new PessoaAtendida();
				
				pessoa.setId((BigInteger)record[7]);
				pessoa.setNome((String)record[8]);
				java.sql.Date dt = ((java.sql.Date)record[9]);
				pessoa.setDataNascimento(dt.toLocalDate());
				int idade = Period.between(pessoa.getDataNascimento(), dataIni).getYears();
				pessoa.setIdade(idade);
				pessoa.setCidade((String)record[10]);
				pessoa.setUf((String)record[11]);
				
				this.mapPessoaAtendida.put(pessoaId, pessoa);
				
			}
			
			hr.setPessoa(pessoa);
			hr.setTipoHospedeId((BigInteger)record[12]);
			hr.setTipoHospedeDescricao((String)record[13]);
			hr.setEntidadeId((BigInteger)record[14]);
			hr.setEntidadeNome((String)record[15]);
			
			this.listResumoHospedagem.add(hr);
		});
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

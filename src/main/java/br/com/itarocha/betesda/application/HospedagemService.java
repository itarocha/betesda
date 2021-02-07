package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.*;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.*;
import br.com.itarocha.betesda.application.port.in.QuartoUseCase;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import br.com.itarocha.betesda.domain.enums.TipoUtilizacaoHospedagemEnum;
import br.com.itarocha.betesda.domain.hospedagem.*;
import br.com.itarocha.betesda.exception.ObsoleteValidationException;
import br.com.itarocha.betesda.util.validation.EntityValidationError;
import br.com.itarocha.betesda.utils.LocalDateUtils;
import br.com.itarocha.betesda.utils.StrUtil;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static br.com.itarocha.betesda.jooq.model.Tables.*;
import static org.jooq.impl.DSL.coalesce;

@Service
@Transactional
public class HospedagemService {

	private static final int QTD_DIAS = 7;
	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private DestinacaoHospedagemJpaRepository destinacaoHospedagemRepo;
	
	@Autowired
	private HospedagemJpaRepository hospedagemRepo;
	
	@Autowired
	private PessoaJpaRepository pessoaRepo;
	
	@Autowired
	private TipoHospedeJpaRepository tipoHospedeRepo;
	
	@Autowired
	private QuartoJpaRepository quartoRepo;
	
	@Autowired
	private LeitoJpaRepository leitoRepo;
	
	@Autowired
	private HospedeLeitoJpaRepository hospedeLeitoRepo;
	
	@Autowired
	private HospedeJpaRepository hospedeRepo;
	
	@Autowired
	private TipoServicoJpaRepository tipoServicoRepo;
	
	@Autowired
	private EntidadeJpaRepository entidadeRepo;
	
	@Autowired
	private EncaminhadorJpaRepository encaminhadorRepo;
	
	@Autowired
	private HospedagemTipoServicoJpaRepository hospedagemTipoServicoRepo;
	
	@Autowired
	private QuartoUseCase quartoService;

	@Autowired
	private DSLContext create;

	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	

	public HospedagemEntity create(HospedagemVO model) throws ObsoleteValidationException {
		HospedagemEntity hospedagemEntity = null;

		LocalDate hoje = LocalDate.now();
			
		if (model.getDataEntrada().isAfter(hoje)) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
					String.format("Data de Entrada não pode ser superior a data atual (%s)",fmt.format(hoje))));
		}
		
		if (model.getDataPrevistaSaida().isBefore(model.getDataEntrada())) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
					String.format("Data Prevista de Saída não pode ser inferior a Data de Entrada (%s)",fmt.format(model.getDataEntrada()))));
		}
		
		for (HospedeVO h : model.getHospedes()) {
			if (!this.pessoaLivreNoPeriodo(h.getPessoaId(), model.getDataEntrada(), model.getDataPrevistaSaida())) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
						String.format("[%s] está em outra hospedagem nesse período", h.getPessoaNome() )));
			}
		}
		
		hospedagemEntity = HospedagemEntity.builder().build();
		
		Optional<EntidadeEntity> entidade = entidadeRepo.findById(model.getEntidadeId());
		hospedagemEntity.setEntidade(entidade.get());
		model.setEntidade(entidade.get());
		
		Optional<EncaminhadorEntity> encaminhador = encaminhadorRepo.findById(model.getEncaminhadorId());
		hospedagemEntity.setEncaminhador(encaminhador.get());
		model.setEncaminhador(encaminhador.get());
		
		hospedagemEntity.setDataEntrada(model.getDataEntrada());
		hospedagemEntity.setDataPrevistaSaida(model.getDataPrevistaSaida());
		
		Optional<DestinacaoHospedagemEntity> dest = destinacaoHospedagemRepo.findById(model.getDestinacaoHospedagemId());
		hospedagemEntity.setDestinacaoHospedagem(dest.get());
		model.setDestinacaoHospedagemDescricao(dest.get().getDescricao());
		
		TipoUtilizacaoHospedagemEnum tu = TipoUtilizacaoHospedagemEnum.valueOf(model.getTipoUtilizacao());
		hospedagemEntity.setTipoUtilizacao(tu);
		hospedagemEntity.setObservacoes(model.getObservacoes());
		
		hospedagemEntity = hospedagemRepo.save(hospedagemEntity);
		
		model.setId(hospedagemEntity.getId());
		
		for (HospedeVO hvo: model.getHospedes()) {
			HospedeEntity h = HospedeEntity.builder().build();
			h.setHospedagem(hospedagemEntity);
			
			Optional<PessoaEntity> p = pessoaRepo.findById(hvo.getPessoaId());
			// se p == null throw
			h.setPessoa(p.get());
			hvo.setPessoaId(p.get().getId());
			hvo.setPessoaNome(p.get().getNome());
			hvo.setPessoaDataNascimento(p.get().getDataNascimento());
			
			Optional<TipoHospedeEntity> th = tipoHospedeRepo.findById(hvo.getTipoHospedeId());
			h.setTipoHospede(th.get());
			hvo.setTipoHospedeDescricao(th.get().getDescricao());
			h = hospedeRepo.save(h);
			hvo.setId(h.getId());
			
			hospedagemEntity.getHospedes().add(h);
			
		    if ((hvo.getAcomodacao() != null) && (TipoUtilizacaoHospedagemEnum.T.equals(hospedagemEntity.getTipoUtilizacao())) ) {
		    	//TODO: Tem um código igual no transferir. Refatorar criar método
		    	Optional<QuartoEntity> quarto = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
		    	Optional<LeitoEntity> leito = leitoRepo.findById(hvo.getAcomodacao().getLeitoId());

		    	if (quarto.isPresent() && leito.isPresent()) {
		    		HospedeLeitoEntity hl = HospedeLeitoEntity.builder().build();
		    		hl.setHospede(h);
		    		hl.setDataEntrada(hospedagemEntity.getDataEntrada());
		    		hl.setDataSaida(hospedagemEntity.getDataPrevistaSaida());

		    		hvo.getAcomodacao().setQuartoNumero(quarto.get().getNumero());
		    		hl.setQuarto(quarto.get());

		    		hvo.getAcomodacao().setLeitoNumero(leito.get().getNumero());
		    		hl.setLeito(leito.get());

		    		hospedeLeitoRepo.save(hl);
		    		hvo.getAcomodacao().setId(hl.getId());
		    		
		    		h.getLeitos().add(hl);
		    	}
		    }
		}
		if ((model.getServicos().length > 0) && (TipoUtilizacaoHospedagemEnum.P.equals(hospedagemEntity.getTipoUtilizacao())) ) {
			for (Long tipoServicoId : model.getServicos()) {
				Optional<TipoServicoEntity> ts = tipoServicoRepo.findById(tipoServicoId);
				if (ts.isPresent()) {
					HospedagemTipoServicoEntity servico = HospedagemTipoServicoEntity.builder().build();
					servico.setTipoServico(ts.get());
					servico.setHospedagem(hospedagemEntity);
					hospedagemTipoServicoRepo.save(servico);
					hospedagemEntity.getServicos().add(servico);
				}
			}
		}	 
		return hospedagemEntity;
	}
	
	private Map<Long, MicroLeito> buildListaLeitos() {
		Map<Long, MicroLeito> _mapLeitos = new TreeMap<>();
		listCabecalhosLeitos().stream().forEach(record -> {
			_mapLeitos.put(record.getId(), new MicroLeito(record.getId(), record.getNumero(), record.getQuartoId(), record.getQuartoNumero() ));
		});
		_mapLeitos.put(99999L, new MicroLeito(9999L, 9999, 9999L, 9999));
		return _mapLeitos;
	}
	
	private Map<Long, MiniLeito> buildListaMiniLeitos() {
		Map<Long, MiniLeito> _mapLeitos = new TreeMap<>();
		listCabecalhosLeitos().stream().forEach(record -> {
			_mapLeitos.put(record.getId(), new MiniLeito(record.getId(), record.getNumero(), record.getQuartoId(), record.getQuartoNumero() ));
		});
		_mapLeitos.put(99999L, new MiniLeito(9999L, 9999, 9999L, 9999));
		return _mapLeitos;
	}

	// Use buildHospedeMapa
	@Deprecated
	private List<HospedeLeitoMapa> buildHospedeLeito(LocalDate dIni, LocalDate dFim) {
		LocalDate hoje = LocalDate.now();

		testeJooq(dIni, dFim);

		StringBuilder sb = StrUtil.loadFile("/sql/hospede_leito_native.sql");
		Query q = em.createNativeQuery(sb.toString())
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );

		List<Object[]> rec =  q.getResultList();
		List<HospedeLeitoMapa> lst = new ArrayList<>();
		
		rec.stream().forEach(record -> {
			HospedeLeitoMapa h = new HospedeLeitoMapa();
			
			
			Long id = recordToLong(record[0]);
			String tipoUtilizacao = recordToString(record[1]);
			String identificador =  String.format("%s%06d", tipoUtilizacao, id); // T000000 Total
			
			h.setIdentificador(identificador);
			h.setTipoUtilizacao(tipoUtilizacao);
			h.setTipoUtilizacaoDescricao("T".equalsIgnoreCase(tipoUtilizacao) ? "Total" : "Parcial");
			h.setQuartoId(recordToLong(record[2]));
			h.setQuartoNumero(recordToInteger(record[3]));
			h.setLeitoId(recordToLong(record[4]));
			h.setLeitoNumero(recordToInteger(record[5]));
			h.setPessoaId(recordToLong(record[6]));
			h.setPessoaNome(recordToString(record[7]));
			h.setPessoaTelefone(recordToString(record[8]));
			
			h.setCidade(recordToString(record[9]));
			h.setUf(recordToString(record[10]));
			
			h.setPessoaCidadeUfOrigem(String.format("%s - %s", h.getCidade(), h.getUf()));
			
			h.setDataEntradaHospedagem(recordSqlSqlDateToLocalDate(record[11]));
			h.setDataSaidaHospedagem(recordSqlSqlDateToLocalDate(record[12]));
			
			h.setDataPrimeiraEntrada(recordSqlSqlDateToLocalDate(record[13]));
			h.setDataUltimaEntrada(recordSqlSqlDateToLocalDate(record[14]));
			
			h.setDataEntradaLeito(recordSqlSqlDateToLocalDate(record[15]));
			h.setDataSaidaLeito(recordSqlSqlDateToLocalDate(record[16]));
			h.setDataIniNoPeriodo(LocalDate.parse(((String)record[17])));
			h.setDataFimNoPeriodo(LocalDate.parse(((String)record[18])));
			h.setHospedagemId(recordToLong(record[19]));
			h.setHospedeId(recordToLong(record[20]));
			h.setTipoHospedeId(recordToLong(record[21]));
			h.setBaixado("S".equals(recordToString(record[22])));
			h.setTipoHospedeDescricao(recordToString(record[23]));
			h.setDestinacaoHospedagemId(recordToLong(record[24]));
			h.setDestinacaoHospedagemDescricao(recordToString(record[25]));
			
			h.setDataPrevistaSaida(recordSqlSqlDateToLocalDate(record[26]));
			h.setDataEfetivaSaida(recordSqlSqlDateToLocalDate(record[27]));
		
			CellStatusHospedagem statusHospedagem = resolveStatusHospedagemNew(hoje, h.getDataPrevistaSaida(), h.getDataEfetivaSaida());
			h.setStatusHospedagem(statusHospedagem);
			
			lst.add(h);
		});
		return lst;
	}

	private List<HospedeMapa> buildHospedeMapa(LocalDate dIni, LocalDate dFim) {
		LocalDate hoje = LocalDate.now();
		
		StringBuilder sb = StrUtil.loadFile("/sql/hospede_leito_native.sql");
		Query q = em.createNativeQuery(sb.toString())
					.setParameter("DATA_INI", dIni )
					.setParameter("DATA_FIM", dFim );

		List<Object[]> rec =  q.getResultList();
		List<HospedeMapa> lst = new ArrayList<>();
		
		rec.stream().forEach(record -> {
			HospedeMapa h = new HospedeMapa();
			
			Long id = recordToLong(record[0]);
			String tipoUtilizacao = recordToString(record[1]);
			String identificador =  String.format("%s%06d", tipoUtilizacao, id); // T000000 Total
			
			h.setIdentificador(identificador);
			h.setTipoUtilizacao(tipoUtilizacao);
			h.setTipoUtilizacaoDescricao("T".equalsIgnoreCase(tipoUtilizacao) ? "Total" : "Parcial");
			h.setQuartoId(recordToLong(record[2]));
			h.setQuartoNumero(recordToInteger(record[3]));
			h.setLeitoId(recordToLong(record[4]));
			h.setLeitoNumero(recordToInteger(record[5]));
			h.setPessoaId(recordToLong(record[6]));
			h.setPessoaNome(recordToString(record[7]));
			h.setPessoaTelefone(recordToString(record[8]));
			
			h.setCidade(recordToString(record[9]));
			h.setUf(recordToString(record[10]));
			
			h.setPessoaCidadeUfOrigem(String.format("%s - %s", h.getCidade(), h.getUf()));
			
			h.setDataEntradaHospedagem(recordSqlSqlDateToLocalDate(record[11]));
			h.setDataSaidaHospedagem(recordSqlSqlDateToLocalDate(record[12]));
			
			h.setDataPrimeiraEntrada(recordSqlSqlDateToLocalDate(record[13]));
			h.setDataUltimaEntrada(recordSqlSqlDateToLocalDate(record[14]));
			
			h.setDataEntradaLeito(recordSqlSqlDateToLocalDate(record[15]));
			h.setDataSaidaLeito(recordSqlSqlDateToLocalDate(record[16]));
			h.setDataIniNoPeriodo(LocalDate.parse(((String)record[17])));
			h.setDataFimNoPeriodo(LocalDate.parse(((String)record[18])));
			h.setHospedagemId(recordToLong(record[19]));
			h.setHospedeId(recordToLong(record[20]));
			h.setTipoHospedeId(recordToLong(record[21]));
			h.setBaixado("S".equals(recordToString(record[22])));
			h.setTipoHospedeDescricao(recordToString(record[23]));
			h.setDestinacaoHospedagemId(recordToLong(record[24]));
			h.setDestinacaoHospedagemDescricao(recordToString(record[25]));
			
			h.setDataPrevistaSaida(recordSqlSqlDateToLocalDate(record[26]));
			h.setDataEfetivaSaida(recordSqlSqlDateToLocalDate(record[27]));
		
			CellStatusHospedagem statusHospedagem = resolveStatusHospedagemNew(hoje, h.getDataPrevistaSaida(), h.getDataEfetivaSaida());
			h.setStatusHospedagem(statusHospedagem);
			
			lst.add(h);
		});
		return lst;
	}

	private String recordToString(Object value) {
		return (String) value;
	}

	private Long recordToLong(Object value) {
		if (value instanceof BigInteger) {
			return ((BigInteger) value).longValue();
		} else 
		if (value instanceof Integer) {
			return ((Integer) value).longValue();
		} else {
			throw new IllegalArgumentException("Não foi possível converter valor para Long. Value = "+value);
		}
	}
	
	private Integer recordToInteger(Object value) {
		//return (Integer) value;
		if (value instanceof BigInteger) {
			return ((BigInteger) value).intValue();
		} else 
		if (value instanceof Integer) {
			return ((Integer) value).intValue();
		} else {
			throw new IllegalArgumentException("Não foi possível converter valor para Integer. Value = "+value);
		}
	}

	private LocalDate recordSqlSqlDateToLocalDate(Object value) {
		if (value == null) {
			return null;
		}
		
		java.sql.Date dt = ((java.sql.Date)value);
		return dt.toLocalDate();
	}
	
	@Deprecated
	public MapaRetorno buildMapaRetorno(LocalDate dataBase) {
		MapaRetorno retorno = new MapaRetorno();

		LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
		LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
		LocalDate hoje = LocalDate.now();
		
		retorno.setDataIni(dIni);
		retorno.setDataFim(dFim);

		//1 - MAPA DE HOSPEDAGENS
		List<HospedeLeitoMapa> listaHospedeLeito = buildHospedeLeito(dIni, dFim);

		Map<Long, MicroLeito> _mapLeitos = buildListaLeitos();
		Map<Long, ArrayList<LinhaHospedagem>>  _mapHospedagens = new HashMap<>(); 
		
		listaHospedeLeito.forEach(hospedeLeito -> {
			int idxIni = Period.between(dIni, hospedeLeito.getDataIniNoPeriodo()).getDays();
			int idxFim = Period.between(dIni, hospedeLeito.getDataFimNoPeriodo()).getDays();
			String classeIni = "???";
			if (hospedeLeito.getDataEntradaLeito().equals(hospedeLeito.getDataIniNoPeriodo()) && hospedeLeito.getDataEntradaLeito().isAfter(hospedeLeito.getDataPrimeiraEntrada())) {
				classeIni = "vindo";
			} else if (hospedeLeito.getDataEntradaLeito().equals(hospedeLeito.getDataIniNoPeriodo())) {
				classeIni = "inicio";
			} else if (hospedeLeito.getDataEntradaLeito().isBefore(hospedeLeito.getDataIniNoPeriodo())) {
				classeIni = "durante";
			}
			String classeFim = "???";
			if (hospedeLeito.getDataSaidaLeito().equals(hospedeLeito.getDataFimNoPeriodo()) && hospedeLeito.getDataSaidaLeito().isBefore(hospedeLeito.getDataUltimaEntrada()) ) {
				classeFim = "indo";
			} else if (hospedeLeito.getDataSaidaLeito().equals(hospedeLeito.getDataFimNoPeriodo()) && hospedeLeito.getBaixado()) {
				classeFim = "baixado";
			} else if (hospedeLeito.getDataSaidaLeito().equals(hospedeLeito.getDataFimNoPeriodo())) {
				classeFim = "fim";
			} else if (hospedeLeito.getDataSaidaLeito().isAfter(hospedeLeito.getDataFimNoPeriodo())) {
				classeFim = "durante";
			}
			Integer[] dias = new Integer[QTD_DIAS];
			for (int i = 0; i < QTD_DIAS; i++) {
				
				if ((idxIni == i) || (i > idxIni && i < idxFim) || (idxFim == i)) {
					dias[i] = 1;
				} else {
					dias[i] = 0;
				}
				
			}
			LinhaHospedagem linhaHospedagem = new LinhaHospedagem();
			
			linhaHospedagem.setIdentificador(hospedeLeito.getIdentificador());
			linhaHospedagem.setHpdId(hospedeLeito.getHospedagemId());
			linhaHospedagem.setStatus(hospedeLeito.getStatusHospedagem().toString());
			linhaHospedagem.setNome(hospedeLeito.getPessoaNome());
			linhaHospedagem.setTelefone(hospedeLeito.getPessoaTelefone());
			linhaHospedagem.setIdxIni(idxIni);
			linhaHospedagem.setIdxFim(idxFim);
			linhaHospedagem.setWidth((idxFim - idxIni + 1)*100);

			linhaHospedagem.setDias(dias);
			hospedeLeito.setDias(dias);

			linhaHospedagem.setClsIni(classeIni.trim());
			linhaHospedagem.setClsFim(classeFim.trim());
			
			if (_mapHospedagens.containsKey(hospedeLeito.getLeitoId())) {
				_mapHospedagens.get(hospedeLeito.getLeitoId()).add(linhaHospedagem);
			} else {
				ArrayList<LinhaHospedagem> lst = new ArrayList<>();
				lst.add(linhaHospedagem);
				_mapHospedagens.put(hospedeLeito.getLeitoId(), lst);
			}
			
		});
		
		_mapLeitos.entrySet().forEach(m -> {
			Long leitoId = m.getKey();
			MicroLeito leito = m.getValue();
			List<LinhaHospedagem> lst = _mapHospedagens.get(leitoId);
			if (lst != null) {
				leito.setHospedagens(lst);
			}
			retorno.getLinhas().add(leito);
		});
		retorno.getLinhas().sort((a, b) -> String.format("%06d-%06d", a.getQuartoNumero(), a.getLeitoNumero())
													.compareTo( String.format("%06d-%06d", b.getQuartoNumero(), b.getLeitoNumero()) ) );
		
		// 2 - HÓSPEDES
		//TODO Criar estrutura pessoa+hospedagem, detalhes
		retorno.setHospedes(listaHospedeLeito);
		retorno.getHospedes().sort((a, b) -> a.getPessoaNome().compareTo(b.getPessoaNome()) );
		
		//INÍCIO
		Map<String, List<String>> porCidade = new TreeMap<>();
		retorno.getHospedes().forEach(h -> {
			if (porCidade.get(h.getPessoaCidadeUfOrigem()) == null ) {
				List<String> lst = new ArrayList<>();
				lst.add(h.getIdentificador());
				porCidade.put(h.getPessoaCidadeUfOrigem(), lst);
			} else {
				List<String> lst = porCidade.get(h.getPessoaCidadeUfOrigem());
				lst.add(h.getIdentificador());
			}
		});

		List<CidadeHospedagens> cidades = new ArrayList<>();
		porCidade.keySet().forEach(k -> {
			cidades.add(new CidadeHospedagens(k, porCidade.get(k)));
		});
		retorno.setCidades(cidades);
		
		// Dias
		LocalDate dtmp = dIni;
		// Qualquer índice abaixo de indiceDataPassada é data inferior a hoje
		int[] indiceDataPassada = {-1};
		while (dtmp.compareTo(dFim) != 1) {
			if (dtmp.isBefore(hoje)) {
				indiceDataPassada[0]++;
			}
			retorno.getDias().add(dtmp);
			dtmp = dtmp.plusDays(1);
		}
		
		//TRATATIVA DO QUADRO
		Integer[] minLeito = {Integer.MAX_VALUE};
		Integer[] maxLeito = {Integer.MIN_VALUE};

		Quadro quadro = new Quadro(); 

		// Busca quartos e leitos do banco
		List<Quarto> listQuartoEntities = quartoService.findAll();
		// Cria os quartos e verifica minimo e maximo dos leitos
		listQuartoEntities.forEach(q -> {
			quadro.quartos.add(new QuadroQuarto( q.getId(), q.getNumero()));
			
			q.getLeitos().forEach(l -> {
				if (l.getNumero() < minLeito[0]) {
					minLeito[0] = l.getNumero();
				} else if (l.getNumero() > maxLeito[0]) {
					maxLeito[0] = l.getNumero();
				}
			});
		});

		// Adiciona todos os leitos em todos os quartos. Todos os leitos estão com id zerado
		quadro.quartos.forEach(q -> {
			for (Integer n = minLeito[0]; n <= maxLeito[0]; n++) {
				q.leitos.add(new QuadroLeito(0L, n, QTD_DIAS));
			}
		});
		
		// Varre mais uma vez os quartos do banco e seta os ids correspondentes. Os ids zerados significa que não existem no banco
		listQuartoEntities.forEach(q -> {
			q.getLeitos().forEach(leito -> {
				quadro.setLeitoIdPorNumero(q.getId(), leito.getNumero(), leito.getId());
			});
		});
		
		quadro.quartos.forEach(q -> {
			q.leitos.forEach(leito -> {
				if (leito.id == 0) {
					//System.out.print("[  ] ");
				} else {
					// Busca o array de linhas de hospedagens.
					// Cada quarto contém um array 
					Long leitoId = leito.id;

					retorno.getLinhas()
					.stream()
					.filter(_leito -> _leito.getLeitoId().equals( leitoId )) // theLeito.getId().equals
					.findFirst()
					.ifPresent(theLeito -> {

						theLeito.getHospedagens().forEach(linhaHpd -> {
							// Quando um índice maior que zero, significa que há hospedagem nesse dia da semana
							for (int i = 0; i < QTD_DIAS; i++) {
								boolean marcar = linhaHpd.getDias()[i] > 0;
								if ( marcar ) {
									leito.getDias()[i] = 1;
								}
							}
						});
					});
				}
				
			});
		});
		
		retorno.setQuadro(quadro);
		
		return retorno;
	}

	public MapaLinhas buildMapaLinhas(LocalDate dataBase) {
		MapaLinhas retorno = new MapaLinhas();

		LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
		LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
		LocalDate hoje = LocalDate.now();
		
		retorno.setDataIni(dIni);
		retorno.setDataFim(dFim);

		//1 - MAPA DE HOSPEDAGENS
		List<HospedeMapa> listaHospedeLeito = buildHospedeMapa(dIni, dFim);

		Map<Long, MiniLeito> _mapLeitos = buildListaMiniLeitos();
		Map<Long, ArrayList<Linha>>  _mapHospedagens = new HashMap<>(); 
		
		listaHospedeLeito.forEach(hospedeLeito -> {
			String classeIni = "???";
			if (hospedeLeito.getDataEntradaLeito().equals(hospedeLeito.getDataIniNoPeriodo()) && hospedeLeito.getDataEntradaLeito().isAfter(hospedeLeito.getDataPrimeiraEntrada())) {
				classeIni = "vindo";
			} else if (hospedeLeito.getDataEntradaLeito().equals(hospedeLeito.getDataIniNoPeriodo())) {
				classeIni = "inicio";
			} else if (hospedeLeito.getDataEntradaLeito().isBefore(hospedeLeito.getDataIniNoPeriodo())) {
				classeIni = "durante";
			}
			String classeFim = "???";
			if (hospedeLeito.getDataSaidaLeito().equals(hospedeLeito.getDataFimNoPeriodo()) && hospedeLeito.getDataSaidaLeito().isBefore(hospedeLeito.getDataUltimaEntrada()) ) {
				classeFim = "indo";
			} else if (hospedeLeito.getDataSaidaLeito().equals(hospedeLeito.getDataFimNoPeriodo()) && hospedeLeito.getBaixado()) {
				classeFim = "baixado";
			} else if (hospedeLeito.getDataSaidaLeito().equals(hospedeLeito.getDataFimNoPeriodo())) {
				classeFim = "fim";
			} else if (hospedeLeito.getDataSaidaLeito().isAfter(hospedeLeito.getDataFimNoPeriodo())) {
				classeFim = "durante";
			}

			Linha linhaHospedagem = new Linha();
			
			linhaHospedagem.setIdentificador(hospedeLeito.getIdentificador());
			linhaHospedagem.setHpdId(hospedeLeito.getHospedagemId());
			linhaHospedagem.setStatus(hospedeLeito.getStatusHospedagem().toString());
			linhaHospedagem.setNome(hospedeLeito.getPessoaNome());
			linhaHospedagem.setTelefone(hospedeLeito.getPessoaTelefone());
			linhaHospedagem.setDataIni(hospedeLeito.getDataIniNoPeriodo());
			linhaHospedagem.setDataFim(hospedeLeito.getDataFimNoPeriodo());

			linhaHospedagem.setClsIni(classeIni.trim());
			linhaHospedagem.setClsFim(classeFim.trim());
			
			if (_mapHospedagens.containsKey(hospedeLeito.getLeitoId())) {
				_mapHospedagens.get(hospedeLeito.getLeitoId()).add(linhaHospedagem);
			} else {
				ArrayList<Linha> lst = new ArrayList<>();
				lst.add(linhaHospedagem);
				_mapHospedagens.put(hospedeLeito.getLeitoId(), lst);
			}
		});
		
		_mapLeitos.entrySet().forEach(m -> {
			Long leitoId = m.getKey();
			MiniLeito leito = m.getValue();
			List<Linha> lst = _mapHospedagens.get(leitoId);
			if (lst != null) {
				leito.setLinhas(lst);
			}
			retorno.getLeitos().add(leito);
		});
		retorno.getLeitos().sort((a, b) -> String.format("%06d-%06d", a.getQuartoNumero(), a.getLeitoNumero())
													.compareTo( String.format("%06d-%06d", b.getQuartoNumero(), b.getLeitoNumero()) ) );
		
		// Dias
		LocalDate dtmp = dIni;
		// Qualquer índice abaixo de indiceDataPassada é data inferior a hoje
		int[] indiceDataPassada = {-1};
		while (dtmp.compareTo(dFim) != 1) {
			if (dtmp.isBefore(hoje)) {
				indiceDataPassada[0]++;
			}
			retorno.getDias().add(dtmp);
			dtmp = dtmp.plusDays(1);
		}
		
		return retorno;
	}

	public MapaHospedes buildMapaHospedes(LocalDate dataBase) {
		MapaHospedes retorno = new MapaHospedes();

		LocalDate dIni = LocalDateUtils.primeiroDiaDaSemana(dataBase);
		LocalDate dFim = dIni.plusDays(QTD_DIAS - 1);
		LocalDate hoje = LocalDate.now();
		
		retorno.setDataIni(dIni);
		retorno.setDataFim(dFim);

		//1 - MAPA DE HOSPEDAGENS
		List<HospedeMapa> listaHospedeLeito = buildHospedeMapa(dIni, dFim);
		
		// Dias
		LocalDate dtmp = dIni;
		// Qualquer índice abaixo de indiceDataPassada é data inferior a hoje
		int[] indiceDataPassada = {-1};
		while (dtmp.compareTo(dFim) != 1) {
			if (dtmp.isBefore(hoje)) {
				indiceDataPassada[0]++;
			}
			retorno.getDias().add(dtmp);
			dtmp = dtmp.plusDays(1);
		}
		
		retorno.setHospedes(listaHospedeLeito);
		retorno.getHospedes().sort((a, b) -> a.getPessoaNome().compareTo(b.getPessoaNome()) );
		
		return retorno;
	}
	
	public MapaCidades buildMapaCidades(LocalDate dataBase) {
		MapaHospedes mapaHospedes = this.buildMapaHospedes(dataBase);
		MapaCidades retorno = new MapaCidades();
		retorno.setDataIni(mapaHospedes.getDataIni());
		retorno.setDataFim(mapaHospedes.getDataFim());
		retorno.setDias(mapaHospedes.getDias());
		
		Map<String, List<HospedeMapa>> porCidade = new TreeMap<>();
		mapaHospedes.getHospedes().forEach(h -> {
			if (porCidade.get(h.getPessoaCidadeUfOrigem()) == null ) {
				List<HospedeMapa> lst = new ArrayList<>();
				lst.add(h);
				porCidade.put(h.getPessoaCidadeUfOrigem(), lst);
			} else {
				List<HospedeMapa> lst = porCidade.get(h.getPessoaCidadeUfOrigem());
				lst.add(h);
			}
		});

		List<HospedagensPorCidade> cidades = new ArrayList<>();
		porCidade.keySet().forEach(k -> {
			cidades.add(new HospedagensPorCidade(k, porCidade.get(k)));
		});
		retorno.setCidades(cidades);
		
		return retorno;
	}

	public MapaQuadro buildMapaQuadro(LocalDate dataBase) {
		MapaHospedes mapaHospedes = this.buildMapaHospedes(dataBase);
		MapaQuadro retorno = new MapaQuadro();
		retorno.setDataIni(mapaHospedes.getDataIni());
		retorno.setDataFim(mapaHospedes.getDataFim());
		retorno.setDias(mapaHospedes.getDias());
		
		//TRATATIVA DO QUADRO
		Integer[] minLeito = {Integer.MAX_VALUE};
		Integer[] maxLeito = {Integer.MIN_VALUE};

		Quadro quadro = new Quadro(); 

		// Busca quartos e leitos do banco
		List<Quarto> listQuartoEntities = quartoService.findAll();
		// Cria os quartos e verifica minimo e maximo dos leitos
		listQuartoEntities.forEach(q -> {
			quadro.quartos.add(new QuadroQuarto( q.getId(), q.getNumero()));
			
			q.getLeitos().forEach(l -> {
				if (l.getNumero() < minLeito[0]) {
					minLeito[0] = l.getNumero();
				} else if (l.getNumero() > maxLeito[0]) {
					maxLeito[0] = l.getNumero();
				}
			});
		});

		// Adiciona todos os leitos em todos os quartos. Todos os leitos estão com id zerado
		quadro.quartos.forEach(q -> {
			for (Integer n = minLeito[0]; n <= maxLeito[0]; n++) {
				q.leitos.add(new QuadroLeito(0L, n, QTD_DIAS));
			}
		});
		
		// Varre mais uma vez os quartos do banco e seta os ids correspondentes. Os ids zerados significa que não existem no banco
		listQuartoEntities.forEach(q -> {
			q.getLeitos().forEach(leito -> {
				quadro.setLeitoIdPorNumero(q.getId(), leito.getNumero(), leito.getId());
			});
		});

		retorno.setQuadro(quadro);
		return retorno;
	}

	public List<OcupacaoLeito> getLeitosOcupadosNoPeriodo(Long hospedagemId, LocalDate dataIni, LocalDate dataFim){
		
		List<BigInteger> todosLeitosNoPeriodo = hospedeLeitoRepo.leitosNoPeriodo(dataIni, dataFim);
		List<BigInteger> hospedagemLeitosNoPeriodo = hospedeLeitoRepo.leitosNoPeriodoPorHospedagem(hospedagemId, dataIni, dataFim);
		
		Map<BigInteger, Boolean> map = new HashMap<>();
		
		List<OcupacaoLeito> lista = new ArrayList<>();
		
		todosLeitosNoPeriodo.forEach(x -> map.put(x, false));
		hospedagemLeitosNoPeriodo.forEach(x -> map.put(x, true));

		map.keySet().forEach( i -> {
			lista.add(new OcupacaoLeito( i.longValue(), map.get(i)));
		});
		
		return lista;
	}
	
	private CellStatusHospedagem resolveStatusHospedagemNew(LocalDate hoje, LocalDate dataPrevistaSaida, LocalDate dataEfetivaSaida) {
		CellStatusHospedagem status = CellStatusHospedagem.ABERTA;
		if (dataEfetivaSaida != null) {
			status = CellStatusHospedagem.ENCERRADA;
		} else if (dataPrevistaSaida.isBefore(hoje) ) {
			status = CellStatusHospedagem.VENCIDA;
		} else {
			status = CellStatusHospedagem.ABERTA;
		}
		return status;
	}

	public HospedagemFullVO getHospedagemPorHospedeLeitoId(Long hospedagemId) {
		HospedagemEntity h = hospedagemRepo.findHospedagemByHospedagemId(hospedagemId);
		HospedagemFullVO retorno = new HospedagemFullVO();
		
		if (h == null) {
			return retorno;
		}
		
		for (HospedagemTipoServicoEntity hts: h.getServicos()) {
			TipoServicoEntity servico = hts.getTipoServico();
			retorno.getServicos().add(servico);
		}
		
		retorno.setId(h.getId());
		retorno.setEntidade(h.getEntidade());
		retorno.setEncaminhador(h.getEncaminhador());
		retorno.setDestinacaoHospedagem(h.getDestinacaoHospedagem());
		retorno.setDataEntrada(h.getDataEntrada());
		retorno.setDataPrevistaSaida(h.getDataPrevistaSaida());
		retorno.setDataEfetivaSaida(h.getDataEfetivaSaida());
		retorno.setTipoUtilizacao(h.getTipoUtilizacao());
		retorno.setObservacoes(h.getObservacoes());
		retorno.setHospedes(h.getHospedes());

		CellStatusHospedagem status = resolveStatusHospedagemNew(LocalDate.now(), h.getDataPrevistaSaida(), h.getDataEfetivaSaida());
		retorno.setStatus(status);

		for (HospedeEntity hospedeEntity : h.getHospedes()) {
			for (HospedeLeitoEntity hl : hospedeEntity.getLeitos()) {
				LeitoDTO leitoDTO = findLeitoByHospedeLeitoId(hl.getId());
				hl.setQuartoNumero( leitoDTO.getQuartoNumero() );
				hl.setLeitoNumero( leitoDTO.getNumero() );
			}
		}
		return retorno;
	}

	public void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento) throws ObsoleteValidationException {
		/*
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: se hospedagem.status == aberta
		* Condição: dataEncerramento >= hospedagem.dataEntrada
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Para cada hospedeLeito - hospedagemLeito.setDataSaída(dataEncerramento)
		* hospedagem.setDataPrevistaSaida(dataEncerramento)
		*/
		Optional<HospedagemEntity> opt  = hospedagemRepo.findById(hospedagemId);
		if (opt.isPresent()) {
			
			HospedagemEntity h = opt.get();
			if ((h.getDataEfetivaSaida() != null)) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem deve ter status = emAberto"));
			}
	
			LocalDate hoje = LocalDate.now();
			
			if (dataEncerramento.isAfter(hoje)) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
						String.format("Data de Encerramento não pode ser superior a data atual (%s)",fmt.format(hoje))));
			}

			if (TipoUtilizacaoHospedagemEnum.P.equals(h.getTipoUtilizacao())) {
				if (h.getDataEntrada().isAfter(dataEncerramento)) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
							String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada (%s)", fmt.format(h.getDataEntrada())) ));
				} 
			} else {
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId);
				if (dataEncerramento.isBefore(dataMinima)) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
							String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada da última movimentação (%s)",fmt.format(dataMinima))));
				}
			}
			
			if (dataEncerramento.isAfter(h.getDataPrevistaSaida())) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
						String.format("Data de Encerramento deve ser inferior a data Prevista de Saída (%s)",fmt.format(h.getDataPrevistaSaida()))));
			}
			
			List<HospedeLeitoEntity> hlToSave = new ArrayList<HospedeLeitoEntity>();
			
			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				List<HospedeEntity> hospedeEntities = h.getHospedes();
				for (HospedeEntity hpd : hospedeEntities) {

					List<HospedeLeitoEntity> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());
					
					for (HospedeLeitoEntity hl : listaHospedeLeitoEntity) {
						if (LogicoEnum.N.equals(hpd.getBaixado())) {
							hlToSave.add(hl);
						}
					}
				}
			}

			for (HospedeLeitoEntity hl : hlToSave) {
				hl.setDataSaida(dataEncerramento);
				hospedeLeitoRepo.save(hl);
			}	
			h.setDataEfetivaSaida(dataEncerramento);
			hospedagemRepo.save(h);
		}
	} 
	
	public void baixarHospede(Long hospedeId, LocalDate dataBaixa) throws ObsoleteValidationException {
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			Long hospedagemId = hospedeOpt.get().getHospedagem().getId();
			HospedeEntity hospedeEntity = hospedeOpt.get();

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hóspede já está baixado"));
			}

			Optional<HospedagemEntity> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				
				HospedagemEntity h = opt.get();
				if ((h.getDataEfetivaSaida() != null)) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem deve ter status = emAberto"));
				}
				
				Long qtd = hospedeLeitoRepo.countHospedesNaoBaixadosByHospedagemId(hospedagemId);
				if (qtd <= 1) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Para baixar hóspede é necessário ter pelo menos 2 hóspedes ativos"));
				}
				
				if (dataBaixa.isBefore(h.getDataEntrada())) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Data de encerramento deve ser superior a data de entrada"));
				}
				
				List<HospedeLeitoEntity> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeitoEntity hl : listaHospedeLeitoEntity) {
					if (hl.getDataEntrada().isAfter(dataBaixa)) {
						throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Existe movimentação com data ANTERIOR a data da baixa"));
					}
					
					hl.setDataSaida(dataBaixa);
					hospedeLeitoRepo.save(hl);
				}
				
				hospedeEntity.setBaixado(LogicoEnum.S);
			}
		}
		
	} 

	public void removerHospede(Long hospedagemId, Long hospedeId) throws ObsoleteValidationException {
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			
			HospedeEntity hospedeEntity = hospedeOpt.get();

			Optional<HospedagemEntity> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				
				HospedagemEntity h = opt.get();
				if ((h.getDataEfetivaSaida() != null)) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem deve ter status = emAberto"));
				}
				
				Long qtd = hospedeLeitoRepo.countHospedesNaoBaixadosByHospedagemId(hospedagemId);
				if (qtd <= 1) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Para remover hóspede é necessário ter pelo menos 2 hóspedes ativos na hospedagem"));
				}
				hospedeRepo.delete(hospedeEntity);
			}
		}
	}
	
	public void alterarTipoHospede(Long hospedeId, Long tipoHospedeId) throws ObsoleteValidationException {
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			HospedeEntity hospedeEntity = hospedeOpt.get();

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hóspede já está baixado"));
			}
	
			Optional<TipoHospedeEntity> th = tipoHospedeRepo.findById(tipoHospedeId);
			hospedeEntity.setTipoHospede(th.get());
			hospedeRepo.save(hospedeEntity);
		}
	} 

	public void transferirHospede(Long hospedeId, Long leitoId, LocalDate dataTransferencia) throws ObsoleteValidationException {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			Long hospedagemId = hospedeOpt.get().getHospedagem().getId();
			HospedeEntity hospedeEntity = hospedeOpt.get();

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hóspede já está baixado"));
			}

			Optional<HospedagemEntity> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				
				HospedagemEntity h = opt.get();
				
				if (!TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Tipo de Utilização da Hospedagem deve ser Total"));
				} 
				
				if ((h.getDataEfetivaSaida() != null)) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem deve ter status = emAberto"));
				}
				
				List<BigInteger> hospedagens = hospedagensNoPeriodo(leitoId, dataTransferencia, h.getDataPrevistaSaida());
				
				for (BigInteger _hospedagemId : hospedagens) {
					Long value = hospedagemId.longValue(); 
					if (!value.equals(h.getId())) {
						throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Este Leito já está em uso no período por outra Hospedagem"));
					}
				}

				if (dataTransferencia.isBefore(h.getDataEntrada())) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Data de encerramento deve ser superior a data de entrada"));
				}
				
				if (dataTransferencia.isAfter(h.getDataPrevistaSaida())) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
							String.format("Data de Transferência deve ser inferior a data Prevista de Saída (%s)",fmt.format(h.getDataPrevistaSaida()))));
				}
				
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId, hospedeId);
				if (dataTransferencia.isBefore(dataMinima.plusDays(1))) {
					throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
							String.format("Data de Transferência deve ser igual ou superior a Data de Entrada da última movimentação (%s)",fmt.format(dataMinima))));
				}
				
				List<HospedeLeitoEntity> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeitoEntity hl : listaHospedeLeitoEntity) {
					if (hl.getDataEntrada().isAfter(dataTransferencia)) {
						throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Existe movimentação com data ANTERIOR a data da transferência"));
					}
					
					hl.setDataSaida(dataTransferencia.minusDays(1));
					hospedeLeitoRepo.save(hl);
				}
				
				// Inserir novo HospedeLeito com LeitoId, dataTransferencia até dataPrevistaSaida
		    	//Optional<Quarto> quarto = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
		    	Optional<LeitoEntity> leito = leitoRepo.findById(leitoId);

		    	if (leito.isPresent()) {
		    		QuartoEntity q = leito.get().getQuarto();
		    		
		    		HospedeLeitoEntity hl = HospedeLeitoEntity.builder().build();
		    		hl.setHospede(hospedeEntity);
		    		hl.setDataEntrada(dataTransferencia);
		    		hl.setDataSaida(h.getDataPrevistaSaida());

		    		hl.setQuarto(q);

		    		hl.setLeito(leito.get());

		    		hospedeLeitoRepo.save(hl);
		    	}
			}
		}
	}
	
	public void adicionarHospede(Long hospedagemId, Long pessoaId, Long tipoHospedeId, Long leitoId, LocalDate dataEntrada) throws ObsoleteValidationException {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		Optional<HospedagemEntity> hospedagemOpt = hospedagemRepo.findById(hospedagemId);
		Optional<PessoaEntity> pessoaOpt = pessoaRepo.findById(pessoaId);
		Optional<LeitoEntity> leitoOpt = leitoRepo.findById(leitoId);
		Optional<TipoHospedeEntity> tipoHospedeOpt = tipoHospedeRepo.findById(tipoHospedeId);
		
		if (!hospedagemOpt.isPresent()) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem não existe"));
		}
		
		if (!pessoaOpt.isPresent()) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Pessoa não cadastrada"));
		}
		
		if (!leitoOpt.isPresent()) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Leito não encontrado"));
		}
		
		HospedagemEntity hospedagemEntity = hospedagemOpt.get();
		if (!TipoUtilizacaoHospedagemEnum.T.equals(hospedagemEntity.getTipoUtilizacao())) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Tipo de Utilização da Hospedagem deve ser Total"));
		} 
		
		if ((hospedagemEntity.getDataEfetivaSaida() != null)) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem deve ter status = emAberto"));
		}

		if (dataEntrada.isBefore(hospedagemEntity.getDataEntrada())){
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Data não pode ser inferior a Data de Início da Hospedagem"));
		}
		
		LocalDate hoje = LocalDate.now();
		
		if (dataEntrada.isAfter(hoje)) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
					String.format("Data de Entrada não pode ser superior a data atual (%s)",fmt.format(hoje))));
		}
		
		if (dataEntrada.isAfter(hospedagemEntity.getDataPrevistaSaida())) {
			throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
					String.format("Data de Entrada deve ser inferior a data Prevista de Saída (%s)",fmt.format(hospedagemEntity.getDataPrevistaSaida()))));
		}
		
		//TODO Hóspede não pode já estar hospedado em algum outro leito no período 
		
		QuartoEntity q = leitoOpt.get().getQuarto();

		HospedeEntity hospedeEntity = HospedeEntity.builder().build();
		hospedeEntity.setHospedagem(hospedagemEntity);
		hospedeEntity.setPessoa(pessoaOpt.get());
		hospedeEntity.setTipoHospede(tipoHospedeOpt.get());
		hospedeEntity = hospedeRepo.save(hospedeEntity);
		
		HospedeLeitoEntity hl = HospedeLeitoEntity.builder().build();
		hl.setHospede(hospedeEntity);
		hl.setDataEntrada(dataEntrada);
		hl.setDataSaida(hospedagemEntity.getDataPrevistaSaida());
		hl.setQuarto(q);
		hl.setLeito(leitoOpt.get());

		hospedeLeitoRepo.save(hl);
		
		hospedagemEntity.getHospedes().add(hospedeEntity);
		hospedagemEntity = hospedagemRepo.save(hospedagemEntity);
	} 
	
	//TODO Implementar renovarHospedagem
	public void renovarHospedagem(Long hospedagemId, LocalDate dataRenovacao) throws ObsoleteValidationException {
		/*
		* Somente se hospedagem.status == aberta
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: novaDataPrevistaSaida > hospedagem.dataPrevistaSaida
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Condição: Verificar se não existe hospedagemLeito em (hospedagemLeito.leito, hospedagem.dataPrevistaSaida + 1, novaDataPrevistaSaida) !!!
		* Para cada hospedeLeito, o último, hospedagemLeito.setDataSaída(novaDataPrevistaSaida)
		* hospedagem.setDataPrevistaSaida(novaDataPrevistaSaida)
		 */
		Optional<HospedagemEntity> opt  = hospedagemRepo.findById(hospedagemId);
		if (opt.isPresent()) {
			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			HospedagemEntity h = opt.get();
			if ((h.getDataEfetivaSaida() != null)) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", "Hospedagem deve ter status = emAberto"));
			}
	
			if (!dataRenovacao.isAfter(h.getDataPrevistaSaida())) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*",
						String.format("Data de Renovação deve ser igual ou superior a Data Prevista de Saída (%s)", fmt.format(h.getDataPrevistaSaida())) ));
			} 

			// Para cada leito (caso seja Total), verificar se ele está sendo utilizado no período (dataPrevistaSaida ~ dataRenovacao)
			
			List<HospedeLeitoEntity> hlToSave = new ArrayList<HospedeLeitoEntity>();

			// Para cada pessoa, verificar se ele está em outra hospedagem no período entre h.getDataPrevistaSaida() e dataRenovacao 
			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				List<HospedeEntity> hospedeEntities = h.getHospedes();
				for (HospedeEntity hpd : hospedeEntities) {
					
					//hpd.getPessoa().getId()
					if (!this.pessoaLivreNoPeriodo(hpd.getPessoa().getId(), h.getDataPrevistaSaida().plusDays(1L), dataRenovacao)) {
						throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", String.format("[%s] está em outra hospedagem nesse novo período", hpd.getPessoa().getNome() )));
					}

					List<HospedeLeitoEntity> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());
					
					for (HospedeLeitoEntity hl : listaHospedeLeitoEntity) {
						if (LogicoEnum.N.equals(hpd.getBaixado())) {
							Long leitoId = hl.getLeito().getId();
							//System.out.println(hpd.getPessoa().getNome() + " - " + hl.getDataSaida() + " - " + hl.getQuarto().getNumero() + " - " + hl.getLeito().getNumero() +  " - Baixado? " + hpd.getBaixado());
							
							List<BigInteger> hospedagens = hospedagensNoPeriodo(leitoId, h.getDataPrevistaSaida().plusDays(1), dataRenovacao);
							
							for (BigInteger _hId : hospedagens) {
								Long value = _hId.longValue(); 
								if (!value.equals( hospedagemId )) {
									Integer quartoNumero = hl.getQuarto().getNumero();
									Integer leitoNumero = hl.getLeito().getNumero() ;

									throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("*", String.format("O Leito %s do Quarto %s já está em uso no período por outra Hospedagem", leitoNumero, quartoNumero)));
								}
							}
							hlToSave.add(hl);
						}
					}
				}
			}

			for (HospedeLeitoEntity hl : hlToSave) {
				hl.setDataSaida(dataRenovacao);
				hospedeLeitoRepo.save(hl);
			}	
			h.setDataPrevistaSaida(dataRenovacao);
			hospedagemRepo.save(h);
		}
		
	}
	
	//TODO Implementar createNaoAtendimento
	public void createNaoAtendimento(Long hospedagemId, LocalDate dataNaoAtendimento) {
		
	}
	
	public void excluirHospedagem(Long id) {
		Optional<HospedagemEntity> opt = hospedagemRepo.findById(id);
		if (opt.isPresent()) {
			hospedagemRepo.delete(opt.get());
		}
	}
	
	//TODO Implementar removeNaoAtendimento
	public void removeNaoAtendimento(Long hospedagemId, Long naoAtendimentoId) {
		
	}
	
	public boolean pessoaLivreNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim) {
		StringBuilder sb = new StringBuilder();
		
		// Verificação em Hospedagem total (possui leito)
		sb.append("SELECT     count(*) "); 
		sb.append("FROM       hospede h ");
		sb.append("INNER JOIN hospede_leito hl ");
		sb.append("ON         hl.hospede_id = h.id ");
		sb.append("WHERE      h.pessoa_id = :pessoaId  "); 
		sb.append("AND        (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim))  "); 
		sb.append("OR         ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim)))  ");
		
		Query query = em.createNativeQuery(sb.toString());
		Long qtd = ((Number)query.setParameter("pessoaId", pessoaId)
								 .setParameter("dataIni", dataIni)
								 .setParameter("dataFim", dataFim)
								 .getSingleResult()).longValue();
		
		StringBuilder sbP = new StringBuilder();
		sbP.append("SELECT     count(*) "); 
		sbP.append("FROM       hospedagem hpd "); 
		sbP.append("INNER JOIN hospede h "); 
		sbP.append("ON         h.hospedagem_id = hpd.id "); 
		sbP.append("WHERE      h.pessoa_id = :pessoaId "); 
		sbP.append("AND        hpd.tipo_utilizacao = :tipoUtilizacao "); 
		sbP.append("AND        (((hpd.data_entrada BETWEEN :dataIni AND :dataFim) OR (COALESCE(hpd.data_efetiva_saida,hpd.data_prevista_saida) BETWEEN :dataIni AND :dataFim)) "); 
		sbP.append("OR          ((hpd.data_entrada <= :dataIni) AND (COALESCE(hpd.data_efetiva_saida,hpd.data_prevista_saida) >= :dataFim))) "); 
		
		Query queryP = em.createNativeQuery(sbP.toString());
		Long qtdP = ((Number)queryP.setParameter("pessoaId", pessoaId)
				 				 .setParameter("tipoUtilizacao", "P")
				 				 .setParameter("dataIni", dataIni)
								 .setParameter("dataFim", dataFim)
								 .getSingleResult()).longValue();
		
		
		return ((qtd <= 0) && (qtdP <= 0)); 
	}
	
	
	public boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT count(*) "); 
		sb.append("FROM   hospede_leito hl "); 
		sb.append("WHERE  hl.leito_id = :leitoId  "); 
		sb.append("AND    (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim))  "); 
		sb.append("OR     ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim)))  ");
		
		Query query = em.createNativeQuery(sb.toString());
		Long qtd = ((Number)query.setParameter("leitoId", leitoId)
								 .setParameter("dataIni", dataIni)
								 .setParameter("dataFim", dataFim)
								 .getSingleResult()).longValue();
		
		return qtd <= 0; 
	}

	public List<BigInteger> hospedagensNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT h.hospedagem_id ");
		sb.append("FROM   hospede_leito hl ");
		sb.append("INNER JOIN hospede h ");
		sb.append("ON         h.id = hl.hospede_id "); 
		sb.append("WHERE  hl.leito_id = :leitoId  "); 
		sb.append("AND    (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim))  "); 
		sb.append("OR     ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim)))  ");
		
		Query query = em.createNativeQuery(sb.toString());
		List<BigInteger> lista = (List<BigInteger>)query.setParameter("leitoId", leitoId)
								 .setParameter("dataIni", dataIni)
								 .setParameter("dataFim", dataFim)
								 .getResultList();
		return lista;
	}

	//TODO Mover para repositório
	private LeitoDTO findLeitoByHospedeLeitoId(Long hospedeLeitoId){
		// Substituindo _OBSOLETO_leito_by_hospede_leito_id.sql
		LeitoDTO retorno =
				create.select(LEITO.ID, LEITO.NUMERO, QUARTO.ID, QUARTO.NUMERO)
						.from(HOSPEDE_LEITO)
						.innerJoin(LEITO).on(HOSPEDE_LEITO.LEITO_ID.eq(LEITO.ID))
						.innerJoin(QUARTO).on(HOSPEDE_LEITO.QUARTO_ID.eq(QUARTO.ID))
						.where(HOSPEDE_LEITO.ID.eq(hospedeLeitoId))
						.fetchOne()
						.map(r -> new LeitoDTO(r.get(LEITO.ID),r.get(LEITO.NUMERO),r.get(QUARTO.ID),r.get(QUARTO.NUMERO)));
		return retorno;
	}

	//TODO Mover para repositório
	private List<LeitoDTO> listCabecalhosLeitos(){
		// Substituindo /sql/_OBSOLETO_leitos_header_native.sql
		return create.select(LEITO.ID, LEITO.NUMERO, QUARTO.ID, QUARTO.NUMERO)
				.from(LEITO)
				.innerJoin(QUARTO).on(LEITO.QUARTO_ID.eq(QUARTO.ID))
				.orderBy(QUARTO.NUMERO, LEITO.NUMERO)
				.fetch()
				.map(r -> new LeitoDTO(r.get(LEITO.ID),r.get(LEITO.NUMERO),r.get(QUARTO.ID),r.get(QUARTO.NUMERO)));
	}

	private void testeJooq(LocalDate dataIni, LocalDate dataFim){
		Condition a = HOSPEDE_LEITO.DATA_ENTRADA.between(dataIni, dataFim).or(HOSPEDE_LEITO.DATA_SAIDA.between(dataIni, dataFim));
		Condition b = HOSPEDE_LEITO.DATA_ENTRADA.lessOrEqual(dataIni).and(HOSPEDE_LEITO.DATA_SAIDA.greaterOrEqual(dataFim));
		Condition condicao = a.or(b);

		System.out.println(

				create.select(HOSPEDE_LEITO.ID, HOSPEDE_LEITO.DATA_ENTRADA, HOSPEDE_LEITO.DATA_SAIDA)
						.from(HOSPEDE_LEITO)
						.where(condicao)
						.fetch()

		);




/*
			WHERE ((hl.data_entrada BETWEEN :DATA_INI and :DATA_FIM) OR (hl.data_saida BETWEEN :DATA_INI and :DATA_FIM))
			OR    ((hl.data_entrada <= :DATA_INI) and (hl.data_saida >= :DATA_FIM))

*/
/*
		WHERE      (((hpd.data_entrada BETWEEN :DATA_INI and :DATA_FIM) OR (COALESCE(hpd.data_efetiva_saida, hpd.data_prevista_saida) BETWEEN :DATA_INI and :DATA_FIM))
		OR          ((hpd.data_entrada <= :DATA_INI) AND (COALESCE(hpd.data_efetiva_saida,hpd.data_prevista_saida) >= :DATA_FIM)))
		AND         (hpd.tipo_utilizacao = 'P')
*/
		//Field<LocalDate> dataSaida = DSL.field(DSL.coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA));
		Condition hpdA1 = HOSPEDAGEM.DATA_ENTRADA.between(dataIni, dataFim)
							.or(coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA)
								.between(dataIni, dataFim));
		Condition hpdA2 = HOSPEDAGEM.DATA_ENTRADA.lessOrEqual(dataIni)
							.and(coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA).ge(dataFim));

		Condition hpdA = hpdA1.or(hpdA2);
		Condition hpd = hpdA.and(HOSPEDAGEM.TIPO_UTILIZACAO.eq("P"));

		System.out.println(

				create.select(	HOSPEDAGEM.ID,
								HOSPEDAGEM.DATA_ENTRADA,
								HOSPEDAGEM.DATA_PREVISTA_SAIDA,
								HOSPEDAGEM.DATA_EFETIVA_SAIDA,
								coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA).as("data_saida_corrigida")
						)
						.from(HOSPEDAGEM)
						.where(hpd)
						.fetch()

		);

	}

}
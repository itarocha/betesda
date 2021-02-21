package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.*;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.*;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.*;
import br.com.itarocha.betesda.application.out.*;
import br.com.itarocha.betesda.application.port.in.HospedagemUseCase;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import br.com.itarocha.betesda.domain.enums.TipoUtilizacaoHospedagemEnum;
import br.com.itarocha.betesda.domain.hospedagem.*;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import br.com.itarocha.betesda.util.validacoes.Violation;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//TODO Tinha mais de 1300 linhas. Deveria ter no máximo 400. Quebrar em diversas classes

@Service
@Transactional
@RequiredArgsConstructor
public class HospedagemService implements HospedagemUseCase {

	private final HospedagemJpaRepository hospedagemRepo;
	private final HospedagemRepositoryAdapter hospedagemRepositoryNew;
	private final PessoaJpaRepository pessoaRepo;
	private final TipoHospedeJpaRepository tipoHospedeRepo;
	private final QuartoJpaRepository quartoRepo;
	private final LeitoJpaRepository leitoRepo;
	private final HospedeLeitoRepository hospedeLeitoRepo;
	private final HospedeJpaRepository hospedeRepo;
	private final DSLContext create;

	// TODO esses mappers devem ser movidos para o HospedagemMapper
	private final EntidadeMapper entidadeMapper;
	private final EncaminhadorMapper encaminhadorMapper;
	private final DestinacaoHospedagemMapper destinacaoHospedagemMapper;
	private final HospedeMapper hospedeMapper;
	private final HospedeLeitoMapper hospedeLeitoMapper;
	private final TipoServicoMapper tipoServicoMapper;
	private final QuartoMapper quartoMapper;
	private final LeitoMapper leitoMapper;

	private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public Hospedagem create(HospedagemNew model) {
		Set<Violation> violations = validateCreateHospedagem(model);
		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
		}
		return hospedagemRepositoryNew.save(model);
	}

	private Set<Violation> validateCreateHospedagem(HospedagemNew model) {
		LocalDate hoje = LocalDate.now();

		Set<Violation> violations = new HashSet<>();

		if (model.getDataEntrada().isAfter(hoje)) {
			violations.add(new Violation("*",
					String.format("Data de Entrada não pode ser superior a data atual (%s)",
							fmt.format(hoje))));
		}

		if (model.getDataPrevistaSaida().isBefore(model.getDataEntrada())) {
			violations.add(new Violation("*",
					String.format("Data Prevista de Saída não pode ser inferior a Data de Entrada (%s)",
							fmt.format(model.getDataEntrada()))));
		}

		for (HospedeNew h : model.getHospedes()) {
			if (!pessoaLivreNoPeriodo(h.getPessoaId(), model.getDataEntrada(), model.getDataPrevistaSaida())) {
				violations.add(new Violation("*",
						String.format("[%s] está em outra hospedagem nesse período", h.getPessoaNome() )));
			}
		}

		if (model.getHospedes().size() == 0) {
			violations.add(new Violation("id", "É necessário pelo menos um hóspede"));
		} else {
			for (HospedeNew h : model.getHospedes()) {
				if ("T".equals(model.getTipoUtilizacao()) && (h.getAcomodacao() == null)) {
					violations.add(new Violation("id", String.format("É necessário informar o Leito para o Hóspede [%s]", h.getPessoaNome())));
				}
				/*
				if (!service.pessoaLivre(h.getPessoaId())) {
					v.addError("id", String.format("[%s] está utilizando uma Hospedagem ainda pendente", h.getPessoaNome()));
				}
				*/
				if ("T".equals(model.getTipoUtilizacao()) && (h.getAcomodacao() != null) &&
						(h.getAcomodacao().getLeitoId() != null) &&
						(model.getDataEntrada() != null) && (model.getDataPrevistaSaida() != null) )
				{
					Long leitoId = h.getAcomodacao().getLeitoId();
					LocalDate dataIni = model.getDataEntrada();
					LocalDate dataFim = model.getDataPrevistaSaida();
					Integer leitoNumero = h.getAcomodacao().getLeitoNumero();
					Integer quartoNumero = h.getAcomodacao().getQuartoNumero();

					if (!this.leitoLivreNoPeriodo(leitoId, dataIni, dataFim)) {
						violations.add(new Violation("id", String.format("Quarto %s Leito %s está ocupado no perído", quartoNumero, leitoNumero )));
					}
				}
			}
		}

		return violations;
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
	
	public Hospedagem getHospedagemPorHospedeLeitoId(Long hospedagemId) {
		HospedagemEntity h = hospedagemRepo.findHospedagemByHospedagemId(hospedagemId);
		Hospedagem retorno = Hospedagem.builder().build();
		
		if (h == null) {
			return retorno;
		}

		h.getServicos()
				.stream()
				.forEach(ts -> retorno.getServicos().add(tipoServicoMapper.toModel(ts.getTipoServico()) ));

		retorno.setId(h.getId());
		retorno.setEntidade(entidadeMapper.toModel(h.getEntidade()));
		retorno.setEncaminhador(encaminhadorMapper.toModel(h.getEncaminhador()));
		retorno.setDestinacaoHospedagem(destinacaoHospedagemMapper.toModel(h.getDestinacaoHospedagem()));
		retorno.setDataEntrada(h.getDataEntrada());
		retorno.setDataPrevistaSaida(h.getDataPrevistaSaida());
		retorno.setDataEfetivaSaida(h.getDataEfetivaSaida());
		retorno.setTipoUtilizacao(h.getTipoUtilizacao());
		retorno.setObservacoes(h.getObservacoes());

		List<Hospede> hospedes = h.getHospedes()
				.stream()
				.map(hospedeMapper::toModel)
				.collect(Collectors.toList());

		retorno.setHospedes(hospedes);

		CellStatusHospedagem status = MapaHospedagemUtil.resolveStatusHospedagemNew(LocalDate.now(), h.getDataPrevistaSaida(), h.getDataEfetivaSaida());
		retorno.setStatus(status);

		for (HospedeEntity hospedeEntity : h.getHospedes()) {
			for (HospedeLeitoEntity hl : hospedeEntity.getLeitos()) {
				LeitoDTO leitoDTO = hospedeLeitoRepo.findLeitoByHospedeLeitoId(hl.getId());
				hl.setQuartoNumero( leitoDTO.getQuartoNumero() );
				hl.setLeitoNumero( leitoDTO.getNumero() );
			}
		}
		return retorno;
	}

	//CRUD
	public void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento) {
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
			Set<Violation> violations = new HashSet<>();
			HospedagemEntity h = opt.get();
			if ((h.getDataEfetivaSaida() != null)) {
				violations.add(new Violation("*", "Hospedagem deve ter status = emAberto"));
			}
	
			LocalDate hoje = LocalDate.now();
			
			if (dataEncerramento.isAfter(hoje)) {
				violations.add(new Violation("*",
						String.format("Data de Encerramento não pode ser superior a data atual (%s)",fmt.format(hoje))));
			}

			if (TipoUtilizacaoHospedagemEnum.P.equals(h.getTipoUtilizacao())) {
				if (h.getDataEntrada().isAfter(dataEncerramento)) {
					violations.add(new Violation("*",
									String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada (%s)",
											fmt.format(h.getDataEntrada())) )
								);
				} 
			} else {
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId);
				if (dataEncerramento.isBefore(dataMinima)) {
					violations.add(new Violation("*",
							String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada da última movimentação (%s)",fmt.format(dataMinima)))
					);
				}
			}
			
			if (dataEncerramento.isAfter(h.getDataPrevistaSaida())) {
				violations.add(new Violation("*",
								String.format("Data de Encerramento deve ser inferior a data Prevista de Saída (%s)",fmt.format(h.getDataPrevistaSaida())))
								);
			}

			if (!violations.isEmpty()){
				throw new EntityValidationException(violations);
			}

			List<HospedeLeito> hlToSave = new ArrayList<>();
			
			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				List<HospedeEntity> hospedeEntities = h.getHospedes();
				for (HospedeEntity hpd : hospedeEntities) {
					List<HospedeLeito> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());

					for (HospedeLeito hl : listaHospedeLeitoEntity) {
						if (LogicoEnum.N.equals(hpd.getBaixado())) {
							hlToSave.add(hl);
						}
					}
				}
			}

			for (HospedeLeito hl : hlToSave) {
				hl.setDataSaida(dataEncerramento);
				hospedeLeitoRepo.save(hl);
			}	
			h.setDataEfetivaSaida(dataEncerramento);
			hospedagemRepo.save(h);
		}
	} 

	//CRUD
	public void baixarHospede(Long hospedeId, LocalDate dataBaixa) {
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			Long hospedagemId = hospedeOpt.get().getHospedagem().getId();
			HospedeEntity hospedeEntity = hospedeOpt.get();

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new EntityValidationException("*", "Hóspede já está baixado");
			}

			Optional<HospedagemEntity> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				Set<Violation> violations = new HashSet<>();

				HospedagemEntity h = opt.get();
				if ((h.getDataEfetivaSaida() != null)) {
					violations.add( new Violation("*", "Hospedagem deve ter status = emAberto"));
				}
				
				Long qtd = hospedeLeitoRepo.countHospedesNaoBaixadosByHospedagemId(hospedagemId);
				if (qtd <= 1) {
					violations.add( new Violation("*", "Para baixar hóspede é necessário ter pelo menos 2 hóspedes ativos"));
				}
				
				if (dataBaixa.isBefore(h.getDataEntrada())) {
					violations.add( new Violation("*", "Data de encerramento deve ser superior a data de entrada"));
				}
				
				List<HospedeLeito> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeito hl : listaHospedeLeitoEntity) {
					if (hl.getDataEntrada().isAfter(dataBaixa)) {
						violations.add( new Violation("*", "Existe movimentação com data ANTERIOR a data da baixa"));
					}

					if (!violations.isEmpty()){
						throw new EntityValidationException(violations);
					}

					hl.setDataSaida(dataBaixa);
					hospedeLeitoRepo.save(hl);
				}
				
				hospedeEntity.setBaixado(LogicoEnum.S);
			}
		}
	}

	//CRUD
	public void removerHospede(Long hospedagemId, Long hospedeId) {
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		hospedeOpt.ifPresent(hospedeEntity -> {
			Optional<HospedagemEntity> opt = hospedagemRepo.findById(hospedagemId);
			opt.ifPresent( h -> {
				Set<Violation> violations = new HashSet<>();
				if ((h.getDataEfetivaSaida() != null)) {
					violations.add(new Violation("*", "Hospedagem deve ter status = emAberto"));
				}
				Long qtd = hospedeLeitoRepo.countHospedesNaoBaixadosByHospedagemId(hospedagemId);
				if (qtd <= 1) {
					violations.add(new Violation("*", "Para remover hóspede é necessário ter pelo menos 2 hóspedes ativos na hospedagem"));
				}
				if (!violations.isEmpty()){
					throw new EntityValidationException(violations);
				}
				hospedeRepo.delete(hospedeEntity);
			});
		});
	}

	//CRUD-
	public void alterarTipoHospede(Long hospedeId, Long tipoHospedeId) {
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		hospedeOpt.ifPresent(hospedeEntity -> {
			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new EntityValidationException("*", "Hóspede já está baixado");
			}
			Optional<TipoHospedeEntity> th = tipoHospedeRepo.findById(tipoHospedeId);
			hospedeEntity.setTipoHospede(th.get());
			hospedeRepo.save(hospedeEntity);
		});
	}

	//CRUD
	public void transferirHospede(Long hospedeId, Long leitoId, LocalDate dataTransferencia)  {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Optional<HospedeEntity> hospedeOpt = hospedeRepo.findById(hospedeId);
		if (hospedeOpt.isPresent()) {
			Long hospedagemId = hospedeOpt.get().getHospedagem().getId();
			HospedeEntity hospedeEntity = hospedeOpt.get();

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new EntityValidationException("*", "Hóspede já está baixado");
			}

			Optional<HospedagemEntity> opt = hospedagemRepo.findById(hospedagemId);
			if (opt.isPresent()) {
				
				HospedagemEntity h = opt.get();
				
				if (!TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
					throw new EntityValidationException("*", "Tipo de Utilização da Hospedagem deve ser Total");
				} 
				
				if ((h.getDataEfetivaSaida() != null)) {
					throw new EntityValidationException("*", "Hospedagem deve ter status = emAberto");
				}
				
				List<Long> hospedagens = hospedeLeitoRepo.hospedagensNoPeriodo(leitoId, dataTransferencia, h.getDataPrevistaSaida());
				for (Long _hospedagemId : hospedagens) {
					if (!_hospedagemId.equals(h.getId())) {
						throw new EntityValidationException("*", "Este Leito já está em uso no período por outra Hospedagem");
					}
				}

				if (dataTransferencia.isBefore(h.getDataEntrada())) {
					throw new EntityValidationException("*", "Data de encerramento deve ser superior a data de entrada");
				}
				
				if (dataTransferencia.isAfter(h.getDataPrevistaSaida())) {
					throw new EntityValidationException("*",
							String.format("Data de Transferência deve ser inferior a data Prevista de Saída (%s)",fmt.format(h.getDataPrevistaSaida())));
				}
				
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId, hospedeId);
				if (dataTransferencia.isBefore(dataMinima.plusDays(1))) {
					throw new EntityValidationException("*",
							String.format("Data de Transferência deve ser igual ou superior a Data de Entrada da última movimentação (%s)",fmt.format(dataMinima)));
				}
				
				List<HospedeLeito> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeito hl : listaHospedeLeitoEntity) {
					if (hl.getDataEntrada().isAfter(dataTransferencia)) {
						throw new EntityValidationException("*", "Existe movimentação com data ANTERIOR a data da transferência");
					}
					
					hl.setDataSaida(dataTransferencia.minusDays(1));
					hospedeLeitoRepo.save(hl);
				}
				
				// Inserir novo HospedeLeito com LeitoId, dataTransferencia até dataPrevistaSaida
		    	//Optional<Quarto> quarto = quartoRepo.findById(hvo.getAcomodacao().getQuartoId());
		    	Optional<LeitoEntity> leito = leitoRepo.findById(leitoId);

				leito.ifPresent(leitoEntity -> {
					QuartoEntity q = leitoEntity.getQuarto();
					HospedeLeitoEntity hl = HospedeLeitoEntity.builder().build();
					hl.setHospede(hospedeEntity);
					hl.setDataEntrada(dataTransferencia);
					hl.setDataSaida(h.getDataPrevistaSaida());
					hl.setQuarto(q);
					hl.setLeito(leitoEntity);
					hospedeLeitoRepo.save(hospedeLeitoMapper.toModel(hl));
				});
			}
		}
	}

	//CRUD
	public void adicionarHospede(Long hospedagemId, Long pessoaId, Long tipoHospedeId, Long leitoId, LocalDate dataEntrada) {
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		Optional<HospedagemEntity> hospedagemOpt = hospedagemRepo.findById(hospedagemId);
		Optional<PessoaEntity> pessoaOpt = pessoaRepo.findById(pessoaId);
		Optional<LeitoEntity> leitoOpt = leitoRepo.findById(leitoId);
		Optional<TipoHospedeEntity> tipoHospedeOpt = tipoHospedeRepo.findById(tipoHospedeId);

		Set<Violation> violations = new HashSet<>();

		if (!hospedagemOpt.isPresent()) {
			violations.add(new Violation("*", "Hospedagem não existe"));
		}
		
		if (!pessoaOpt.isPresent()) {
			violations.add(new Violation("*", "Pessoa não cadastrada"));
		}
		
		if (!leitoOpt.isPresent()) {
			violations.add(new Violation("*", "Leito não encontrado"));
		}
		
		HospedagemEntity hospedagemEntity = hospedagemOpt.get();
		if (!TipoUtilizacaoHospedagemEnum.T.equals(hospedagemEntity.getTipoUtilizacao())) {
			violations.add(new Violation("*", "Tipo de Utilização da Hospedagem deve ser Total"));
		} 
		
		if ((hospedagemEntity.getDataEfetivaSaida() != null)) {
			violations.add(new Violation("*", "Hospedagem deve ter status = emAberto"));
		}

		if (dataEntrada.isBefore(hospedagemEntity.getDataEntrada())){
			violations.add(new Violation("*", "Data não pode ser inferior a Data de Início da Hospedagem"));
		}
		
		LocalDate hoje = LocalDate.now();
		
		if (dataEntrada.isAfter(hoje)) {
			violations.add(new Violation("*", String.format("Data de Entrada não pode ser superior a data atual (%s)",fmt.format(hoje))));
		}
		
		if (dataEntrada.isAfter(hospedagemEntity.getDataPrevistaSaida())) {
			violations.add(new Violation("*",
					String.format("Data de Entrada deve ser inferior a data Prevista de Saída (%s)",fmt.format(hospedagemEntity.getDataPrevistaSaida())))
			);
		}

		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
		}

		//TODO Hóspede não pode já estar hospedado em algum outro leito no período 
		
		QuartoEntity q = leitoOpt.get().getQuarto();

		HospedeEntity hospedeEntity = HospedeEntity.builder()
				.hospedagem(hospedagemEntity)
				.pessoa(pessoaOpt.get())
				.tipoHospede(tipoHospedeOpt.get())
				.build();
		hospedeEntity = hospedeRepo.save(hospedeEntity);
		
		HospedeLeito hl = HospedeLeito.builder()
				.hospede(hospedeMapper.toModel(hospedeEntity))
				.dataEntrada(dataEntrada)
				.dataSaida(hospedagemEntity.getDataPrevistaSaida())
				.quarto(quartoMapper.toModel(q))
				.leito(leitoMapper.toModel(leitoOpt.get()))
				.build();

		hospedeLeitoRepo.save(hl);
		
		hospedagemEntity.getHospedes().add(hospedeEntity);
		hospedagemEntity = hospedagemRepo.save(hospedagemEntity);
	} 

	//CRUD
	//TODO Implementar renovarHospedagem
	public void renovarHospedagem(Long hospedagemId, LocalDate dataRenovacao) {
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
				throw new EntityValidationException("*", "Hospedagem deve ter status = emAberto");
			}
	
			if (!dataRenovacao.isAfter(h.getDataPrevistaSaida())) {
				throw new EntityValidationException("*",
						String.format("Data de Renovação deve ser igual ou superior a Data Prevista de Saída (%s)", fmt.format(h.getDataPrevistaSaida())) );
			} 

			// Para cada leito (caso seja Total), verificar se ele está sendo utilizado no período (dataPrevistaSaida ~ dataRenovacao)
			
			List<HospedeLeito> hlToSave = new ArrayList<>();

			// Para cada pessoa, verificar se ele está em outra hospedagem no período entre h.getDataPrevistaSaida() e dataRenovacao 
			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				List<HospedeEntity> hospedeEntities = h.getHospedes();
				for (HospedeEntity hpd : hospedeEntities) {
					
					//hpd.getPessoa().getId()
					if (!this.pessoaLivreNoPeriodo(hpd.getPessoa().getId(), h.getDataPrevistaSaida().plusDays(1L), dataRenovacao)) {
						throw new EntityValidationException("*", String.format("[%s] está em outra hospedagem nesse novo período", hpd.getPessoa().getNome() ));
					}

					List<HospedeLeito> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());
					
					for (HospedeLeito hl : listaHospedeLeitoEntity) {
						if (LogicoEnum.N.equals(hpd.getBaixado())) {
							Long leitoId = hl.getLeito().getId();
							//System.out.println(hpd.getPessoa().getNome() + " - " + hl.getDataSaida() + " - " + hl.getQuarto().getNumero() + " - " + hl.getLeito().getNumero() +  " - Baixado? " + hpd.getBaixado());
							
							List<Long> hospedagens = hospedeLeitoRepo.hospedagensNoPeriodo(leitoId, h.getDataPrevistaSaida().plusDays(1), dataRenovacao);
							
							for (Long _hId : hospedagens) {
								if (!_hId.equals( hospedagemId )) {
									Integer quartoNumero = hl.getQuarto().getNumero();
									Integer leitoNumero = hl.getLeito().getNumero() ;

									throw new EntityValidationException("*", String.format("O Leito %s do Quarto %s já está em uso no período por outra Hospedagem", leitoNumero, quartoNumero));
								}
							}
							hlToSave.add(hl);
						}
					}
				}
			}

			for (HospedeLeito hl : hlToSave) {
				hl.setDataSaida(dataRenovacao);
				hospedeLeitoRepo.save(hl);
			}	
			h.setDataPrevistaSaida(dataRenovacao);
			hospedagemRepo.save(h);
		}
		
	}

	//CRUD
	//TODO Implementar createNaoAtendimento
	public void createNaoAtendimento(Long hospedagemId, LocalDate dataNaoAtendimento) {
		
	}

	//CRUD
	public void excluirHospedagem(Long id) {
		hospedagemRepo.findById(id).ifPresent(h -> hospedagemRepo.delete(h));
	}

	//CRUD
	//TODO Implementar removeNaoAtendimento
	public void removeNaoAtendimento(Long hospedagemId, Long naoAtendimentoId) {
		
	}

	public boolean pessoaLivreNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim) {
		Long qtd = hospedeLeitoRepo.hospedagensDePessoaNoPeriodo(pessoaId, dataIni, dataFim).stream().count();
		Integer qtdP = hospedeLeitoRepo.countOfHospedagensParciaisDePessoaNoPeriodo(pessoaId, dataIni, dataFim);
		return ((qtd <= 0) && (qtdP <= 0));
	}

	@Override
	public boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		return hospedeLeitoRepo.leitoLivreNoPeriodo(leitoId, dataIni, dataFim);
	}
}
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

//TODO Tinha mais de 1300 linhas. Deveria ter no máximo 400. Quebrar em diversas classes

@Service
@Transactional
@RequiredArgsConstructor
public class HospedagemService implements HospedagemUseCase {

	private final HospedagemJpaRepository hospedagemRepo;
	private final HospedagemRepositoryAdapter hospedagemRepositoryNew;
	private final PessoaJpaRepository pessoaRepo;
	private final TipoHospedeJpaRepository tipoHospedeRepo;
	private final LeitoJpaRepository leitoRepo;
	private final HospedeLeitoRepository hospedeLeitoRepo;
	private final HospedeJpaRepository hospedeRepo;
	private final HospedagemMapper hospedagemMapper;

	// TODO esses mappers devem ser movidos para o HospedagemMapper
	private final HospedeMapper hospedeMapper;
	private final HospedeLeitoMapper hospedeLeitoMapper;
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

		model.getHospedes()
				.stream()
				.filter(h -> !pessoaLivreNoPeriodo(h.getPessoaId(), model.getDataEntrada(), model.getDataPrevistaSaida()))
				.forEach(h -> addViolation(	violations,
									"*",
											String.format("[%s] está em outra hospedagem nesse período", h.getPessoaNome() )));

		if (model.getHospedes().isEmpty()) {
			violations.add(new Violation("id", "É necessário pelo menos um hóspede"));
		}

		model.getHospedes()
				.stream()
				.forEach(h -> {
						if (TipoUtilizacaoHospedagemEnum.T.equals(model.getTipoUtilizacao()) &&
								(isNull(h.getAcomodacao())))
						{
							addViolation(violations,
									"id",
									String.format("É necessário informar o Leito para o Hóspede [%s]", h.getPessoaNome()));
						}
						/*
						if (!service.pessoaLivre(h.getPessoaId())) {
							v.addError("id", String.format("[%s] está utilizando uma Hospedagem ainda pendente", h.getPessoaNome()));
						}
						*/
						if (	TipoUtilizacaoHospedagemEnum.T.equals(model.getTipoUtilizacao()) &&
								(!isNull(h.getAcomodacao())) &&
								(h.getAcomodacao().getLeitoId() != null) &&
								(!isNull(model.getDataEntrada())) &&
								(!isNull(model.getDataPrevistaSaida())) )
						{
							Long leitoId = h.getAcomodacao().getLeitoId();
							LocalDate dataIni = model.getDataEntrada();
							LocalDate dataFim = model.getDataPrevistaSaida();

							if (leitoOcupadoNoPeriodo(leitoId, dataIni, dataFim)) {
								addViolation(violations, "id", String.format("Quarto %s Leito %s está ocupado no perído",
										h.getAcomodacao().getQuartoNumero(),
										h.getAcomodacao().getLeitoNumero() ));
							}
						}
				});

		return violations;
	}

	public List<OcupacaoLeito> getLeitosOcupadosNoPeriodo(Long hospedagemId, LocalDate dataIni, LocalDate dataFim){
		List<BigInteger> todosLeitosNoPeriodo = hospedeLeitoRepo.leitosNoPeriodo(dataIni, dataFim);
		List<BigInteger> hospedagemLeitosNoPeriodo = hospedeLeitoRepo.leitosNoPeriodoPorHospedagem(hospedagemId, dataIni, dataFim);
		
		Map<BigInteger, Boolean> map = new HashMap<>();
		
		List<OcupacaoLeito> lista = new ArrayList<>();
		
		todosLeitosNoPeriodo.forEach(x -> map.put(x, false));
		hospedagemLeitosNoPeriodo.forEach(x -> map.put(x, true));

		//Map<BigInteger, Boolean> mapF = todosLeitosNoPeriodo.stream().collect(Collectors.toMap(x -> x, x -> false));
		//Map<BigInteger, Boolean> mapT = hospedagemLeitosNoPeriodo.stream().collect(Collectors.toMap(x -> x, x -> false));

		map.keySet().forEach( i -> {
			lista.add(new OcupacaoLeito( i.longValue(), map.get(i)));
		});

		return map.entrySet()
				.stream()
				.map(entry -> new OcupacaoLeito(entry.getKey().longValue(), entry.getValue()))
				.collect(Collectors.toList());
	}
	
	public Hospedagem getHospedagemPorHospedeLeitoId(Long hospedagemId) {
		return hospedagemMapper.toModel(hospedagemRepo.findHospedagemByHospedagemId(hospedagemId));
	}

	public void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento) {
		/*
		* Condição: se hospedagem.status == aberta
		* Condição: dataEncerramento >= hospedagem.dataEntrada
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Para cada hospedeLeito - hospedagemLeito.setDataSaída(dataEncerramento)
		* hospedagem.setDataPrevistaSaida(dataEncerramento)
		*/

		Optional<HospedagemEntity> hospedagemOpt  = hospedagemRepo.findById(hospedagemId);

		hospedagemOpt.ifPresent(h -> {
			Set<Violation> violations = new HashSet<>();
			if ((h.getDataEfetivaSaida() != null)) {
				addViolation(violations, "*", "Hospedagem deve ter status = emAberto");
			}

			LocalDate hoje = LocalDate.now();
			if (dataEncerramento.isAfter(hoje)) {
				addViolation(violations, "*", String.format("Data de Encerramento não pode ser superior a data atual (%s)",
														fmt.format(hoje)));
			}

			if ((TipoUtilizacaoHospedagemEnum.P.equals(h.getTipoUtilizacao())) && (h.getDataEntrada().isAfter(dataEncerramento))) {
				addViolation(violations, "*", String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada (%s)",
														fmt.format(h.getDataEntrada())));

			}

			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				LocalDate dataMinima = hospedeLeitoRepo.ultimaDataEntradaByHospedagemId(hospedagemId);
				if (dataEncerramento.isBefore(dataMinima)) {
					addViolation( violations,"*", String.format("Data de Encerramento deve ser igual ou superior a Data de Entrada da última movimentação (%s)",
															fmt.format(dataMinima)));
				}
			}

			if (dataEncerramento.isAfter(h.getDataPrevistaSaida())) {
				addViolation(violations, "*", String.format("Data de Encerramento deve ser inferior a data Prevista de Saída (%s)",
														fmt.format(h.getDataPrevistaSaida())));
			}

			if (!violations.isEmpty()){
				throw new EntityValidationException(violations);
			}

			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				List<HospedeLeito> hlToSave = new ArrayList<>();
				h.getHospedes()
						.stream()
						.filter(hpd -> LogicoEnum.N.equals(hpd.getBaixado()))
						.forEach(hpd -> {
									hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId())
											.stream()
											.forEach(hl -> hlToSave.add(hl));
								});

				hlToSave.stream().forEach(hl -> {
					hl.setDataSaida(dataEncerramento);
					hospedeLeitoRepo.save(hl);
				});
			}
			h.setDataEfetivaSaida(dataEncerramento);
			hospedagemRepo.save(h);
		});
	}

	public void baixarHospede(Long hospedeId, LocalDate dataBaixa) {
		Optional<HospedeEntity> hospedeOptional = hospedeRepo.findById(hospedeId);

		hospedeOptional.ifPresent(hospedeEntity -> {
			Long hospedagemId = hospedeEntity.getId();

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new EntityValidationException("*", "Hóspede já está baixado");
			}

			hospedagemRepo.findById(hospedagemId).ifPresent(h -> {
				Set<Violation> violations = new HashSet<>();

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

				listaHospedeLeitoEntity.stream()
						.filter(hl -> hl.getDataEntrada().isAfter(dataBaixa))
						.forEach(hl -> violations.add( new Violation("*","Existe movimentação com data ANTERIOR a data da baixa")));

				if (!violations.isEmpty()){
					throw new EntityValidationException(violations);
				}

				listaHospedeLeitoEntity.stream().forEach(hl -> {
					hl.setDataSaida(dataBaixa);
					hospedeLeitoRepo.save(hl);
				});

				hospedeEntity.setBaixado(LogicoEnum.S);
			});
		});
	}

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

	public void transferirHospede(Long hospedeId, Long leitoId, LocalDate dataTransferencia)  {
		hospedeRepo.findById(hospedeId).ifPresent(hospedeEntity -> {

			if ((LogicoEnum.S.equals(hospedeEntity.getBaixado())  )) {
				throw new EntityValidationException("*", "Hóspede já está baixado");
			}

			Long hospedagemId = hospedeEntity.getHospedagem().getId();

			hospedagemRepo.findById(hospedagemId).ifPresent( h -> {

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
				leitoRepo.findById(leitoId)
						.ifPresent(leitoEntity -> {
							QuartoEntity q = leitoEntity.getQuarto();
							HospedeLeitoEntity hl = HospedeLeitoEntity.builder().build();
							hl.setHospede(hospedeEntity);
							hl.setDataEntrada(dataTransferencia);
							hl.setDataSaida(h.getDataPrevistaSaida());
							hl.setQuarto(q);
							hl.setLeito(leitoEntity);
							hospedeLeitoRepo.save(hospedeLeitoMapper.toModel(hl));
						});
			});

		});
	}

	public void adicionarHospede(Long hospedagemId, Long pessoaId, Long tipoHospedeId, Long leitoId, LocalDate dataEntrada) {
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

	//TODO Implementar renovarHospedagem
	public void renovarHospedagem(Long hospedagemId, LocalDate dataRenovacao) {
		/*
		* Somente se hospedagem.status == aberta
		* hospedagem = getHospedagem(hospedagemId)
		* Condição: dataRenovacao > hospedagem.dataPrevistaSaida
		* hospedagemLeito = getUltimoHospedagemLeito(hospedagemId)
		* Condição: Verificar se não existe hospedagemLeito em (hospedagemLeito.leito, hospedagem.dataPrevistaSaida + 1, novaDataPrevistaSaida) !!!
		* Para cada hospedeLeito, setar o último hospedagemLeito.setDataSaída(novaDataPrevistaSaida)
		* hospedagem.setDataPrevistaSaida(novaDataPrevistaSaida)
		 */
		hospedagemRepo.findById(hospedagemId).ifPresent(hospedagemEntity -> {

			if ((hospedagemEntity.getDataEfetivaSaida() != null)) {
				throw new EntityValidationException("*", "Hospedagem deve ter status = emAberto");
			}
	
			if (!dataRenovacao.isAfter(hospedagemEntity.getDataPrevistaSaida())) {
				throw new EntityValidationException("*",
						String.format("Data de Renovação deve ser igual ou superior a Data Prevista de Saída (%s)", fmt.format(hospedagemEntity.getDataPrevistaSaida())) );
			}

			List<HospedeLeito> hlToSave = new ArrayList<>();

			// Para cada leito (caso seja Total), verificar se ele está sendo utilizado no período (dataPrevistaSaida ~ dataRenovacao)
			// Para cada pessoa, verificar se ele está em outra hospedagem no período entre h.getDataPrevistaSaida() e dataRenovacao
			if (TipoUtilizacaoHospedagemEnum.T.equals(hospedagemEntity.getTipoUtilizacao())) {
				hospedagemEntity.getHospedes().stream().forEach(hpd -> {
					if (!this.pessoaLivreNoPeriodo(hpd.getPessoa().getId(), hospedagemEntity.getDataPrevistaSaida().plusDays(1), dataRenovacao)) {
						throw new EntityValidationException("*",
								String.format("[%s] está em outra hospedagem nesse novo período", hpd.getPessoa().getNome() ));
					}

					List<HospedeLeito> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hpd.getId());

					listaHospedeLeitoEntity.stream()
							.forEach(hl -> {
								if (LogicoEnum.N.equals(hpd.getBaixado())) {
									Long leitoId = hl.getLeito().getId();

									List<Long> listaHospedeId = hospedeLeitoRepo.hospedagensNoPeriodo(	leitoId,
											hospedagemEntity.getDataPrevistaSaida().plusDays(1),
											dataRenovacao);

									listaHospedeId.stream()
											.filter(id -> !id.equals(hospedagemId))
											.forEach(idHospedagem -> {
												throw new EntityValidationException("*",
															String.format(	"O Leito %s do Quarto %s já está em uso no período por outra Hospedagem",
																			hl.getLeito().getNumero(),
																			hl.getQuarto().getNumero()));
											});
									hlToSave.add(hl);
								}
				 			});
				});
			}
			hlToSave.stream().forEach(hl -> {
				hospedeLeitoRepo.updateDataSaida(hl.getId(), dataRenovacao);
			});

			hospedagemRepo.updateDataPrevistaSaida(hospedagemEntity.getId(), dataRenovacao);
		});
	}

	//TODO Implementar createNaoAtendimento
	public void createNaoAtendimento(Long hospedagemId, LocalDate dataNaoAtendimento) {
		
	}

	public void excluirHospedagem(Long id) {
		hospedagemRepo.findById(id).ifPresent(h -> hospedagemRepo.delete(h));
	}

	//TODO Implementar removeNaoAtendimento
	public void removeNaoAtendimento(Long hospedagemId, Long naoAtendimentoId) {
		
	}

	public boolean pessoaLivreNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim) {
		Boolean semHospedagensDePessoasNoPeriodo = hospedeLeitoRepo.hospedagensDePessoaNoPeriodo(pessoaId, dataIni, dataFim).isEmpty();
		Boolean semHospedagensParciais = hospedeLeitoRepo.countOfHospedagensParciaisDePessoaNoPeriodo(pessoaId, dataIni, dataFim) <= 0;
		return semHospedagensDePessoasNoPeriodo && semHospedagensParciais;
	}

	@Override
	public boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		return hospedeLeitoRepo.leitoLivreNoPeriodo(leitoId, dataIni, dataFim);
	}

	private Set<Violation> addViolation(Set<Violation> violations, String fieldName, String message){
		violations.add(new Violation(fieldName, message));
		return violations;
	}

	private boolean leitoOcupadoNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim){
		return !this.leitoLivreNoPeriodo(leitoId, dataIni, dataFim);
	}

}
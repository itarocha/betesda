package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.*;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.*;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.*;
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
import javax.persistence.Query;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.itarocha.betesda.jooq.model.Tables.*;

//TODO Tinha mais de 1300 linhas. Deveria ter no máximo 400. Quebrar em diversas classes

@Service
@Transactional
@RequiredArgsConstructor
public class HospedagemService implements HospedagemUseCase {

	private final EntityManager em;
	private final DestinacaoHospedagemJpaRepository destinacaoHospedagemRepo;
	private final HospedagemJpaRepository hospedagemRepo;
	private final PessoaJpaRepository pessoaRepo;
	private final TipoHospedeJpaRepository tipoHospedeRepo;
	private final QuartoJpaRepository quartoRepo;
	private final LeitoJpaRepository leitoRepo;
	private final HospedeLeitoJpaRepository hospedeLeitoRepo;
	private final HospedeJpaRepository hospedeRepo;
	private final TipoServicoJpaRepository tipoServicoRepo;
	private final EntidadeJpaRepository entidadeRepo;
	private final EncaminhadorJpaRepository encaminhadorRepo;
	private final HospedagemTipoServicoJpaRepository hospedagemTipoServicoRepo;
	private final DSLContext create;

	// TODO esses mappers devem ser movidos para o HospedagemMapper
	private final EntidadeMapper entidadeMapper;
	private final EncaminhadorMapper encaminhadorMapper;
	private final DestinacaoHospedagemMapper destinacaoHospedagemMapper;
	private final HospedeMapper hospedeMapper;
	private final TipoServicoMapper tipoServicoMapper;

	//private static final int QTD_DIAS = 7;
	private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	//CRUD
	public HospedagemEntity create(HospedagemVO model) {
		HospedagemEntity hospedagemEntity = null;

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

		for (HospedeVO h : model.getHospedes()) {
			if (!pessoaLivreNoPeriodo(h.getPessoaId(), model.getDataEntrada(), model.getDataPrevistaSaida())) {
				violations.add(new Violation("*",
						String.format("[%s] está em outra hospedagem nesse período", h.getPessoaNome() )));
			}
		}

		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
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
	
	public HospedagemFullVO getHospedagemPorHospedeLeitoId(Long hospedagemId) {
		HospedagemEntity h = hospedagemRepo.findHospedagemByHospedagemId(hospedagemId);
		HospedagemFullVO retorno = HospedagemFullVO.builder().build();
		
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
				LeitoDTO leitoDTO = findLeitoByHospedeLeitoId(hl.getId());
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
				
				List<HospedeLeitoEntity> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeitoEntity hl : listaHospedeLeitoEntity) {
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
				
				List<BigInteger> hospedagens = hospedagensNoPeriodo(leitoId, dataTransferencia, h.getDataPrevistaSaida());
				
				for (BigInteger _hospedagemId : hospedagens) {
					Long value = hospedagemId.longValue(); 
					if (!value.equals(h.getId())) {
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
				
				List<HospedeLeitoEntity> listaHospedeLeitoEntity = hospedeLeitoRepo.findUltimoByHospedeId(hospedeId);
				for (HospedeLeitoEntity hl : listaHospedeLeitoEntity) {
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
					hospedeLeitoRepo.save(hl);
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
		
		HospedeLeitoEntity hl = HospedeLeitoEntity.builder()
				.hospede(hospedeEntity)
				.dataEntrada(dataEntrada)
				.dataSaida(hospedagemEntity.getDataPrevistaSaida())
				.quarto(q)
				.leito(leitoOpt.get())
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
			
			List<HospedeLeitoEntity> hlToSave = new ArrayList<HospedeLeitoEntity>();

			// Para cada pessoa, verificar se ele está em outra hospedagem no período entre h.getDataPrevistaSaida() e dataRenovacao 
			if (TipoUtilizacaoHospedagemEnum.T.equals(h.getTipoUtilizacao())) {
				List<HospedeEntity> hospedeEntities = h.getHospedes();
				for (HospedeEntity hpd : hospedeEntities) {
					
					//hpd.getPessoa().getId()
					if (!this.pessoaLivreNoPeriodo(hpd.getPessoa().getId(), h.getDataPrevistaSaida().plusDays(1L), dataRenovacao)) {
						throw new EntityValidationException("*", String.format("[%s] está em outra hospedagem nesse novo período", hpd.getPessoa().getNome() ));
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

									throw new EntityValidationException("*", String.format("O Leito %s do Quarto %s já está em uso no período por outra Hospedagem", leitoNumero, quartoNumero));
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

	//CRUD
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
	
	//CRUD
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

	//CRUD
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

}
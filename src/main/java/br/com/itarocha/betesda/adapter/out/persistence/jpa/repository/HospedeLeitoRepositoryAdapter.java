package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.HospedeLeitoMapper;
import br.com.itarocha.betesda.core.ports.out.HospedeLeitoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.HospedeLeito;
import br.com.itarocha.betesda.domain.LeitoDTO;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.itarocha.betesda.jooq.model.Tables.*;
import static br.com.itarocha.betesda.jooq.model.Tables.QUARTO;
import static org.jooq.impl.DSL.coalesce;
import static org.jooq.impl.DSL.count;

@Service
@RequiredArgsConstructor
public class HospedeLeitoRepositoryAdapter implements HospedeLeitoRepository {

	private final HospedeLeitoJpaRepository repository;
	private final HospedeLeitoMapper mapper;
	private final DSLContext create;

	@Override
	public HospedeLeito save(HospedeLeito model) {
		try {
			return mapper.toModel(repository.save(mapper.toEntity(model)));
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar gravar HospedeLeito"
					, e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public Optional<HospedeLeito> findById(Long id) {
		Optional<HospedeLeitoEntity> opt = repository.findById(id);
		return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
	}

	@Override
	public void delete(HospedeLeito model) {
		try {
			repository.delete(mapper.toEntity(model));
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar excluir HospedeLeito"
					, e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar excluir HospedeLeito"
					, e.getMostSpecificCause().getMessage());
		}
	}

	public List<HospedeLeito> findUltimoByHospedagemId(Long hospedagemId){
		return repository.findUltimoByHospedagemId(hospedagemId)
				.stream()
				.map(mapper::toModel)
				.collect(Collectors.toList());
	}
	
	public List<HospedeLeito> findUltimoByHospedeId(Long hospedeId){
		return repository.findUltimoByHospedeId(hospedeId)
				.stream()
				.map(mapper::toModel)
				.collect(Collectors.toList());
	}
	
	public List<HospedeLeito> findByLeitoNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim){
		return repository.findByLeitoNoPeriodo(leitoId, dataIni, dataFim)
				.stream()
				.map(mapper::toModel)
				.collect(Collectors.toList());
	}

	public Long countHospedesNaoBaixadosByHospedagemId(Long hospedagemId){
		return repository.countHospedesNaoBaixadosByHospedagemId(hospedagemId);
	}

	public LocalDate ultimaDataEntradaByHospedagemId(Long hospedagemId){
		return repository.ultimaDataEntradaByHospedagemId(hospedagemId);
	}
	
	public LocalDate ultimaDataEntradaByHospedagemId(Long hospedagemId, Long hospedeId){
		return repository.ultimaDataEntradaByHospedagemId(hospedagemId, hospedeId);
	}
	
	public List<BigInteger> leitosNoPeriodo(LocalDate dataIni, LocalDate dataFim){
		return repository.leitosNoPeriodo(dataIni, dataFim);
	}
	
	public List<BigInteger> leitosNoPeriodoPorHospedagem(Long hospedagemId, LocalDate dataIni, LocalDate dataFim){
		return repository.leitosNoPeriodoPorHospedagem(hospedagemId, dataIni, dataFim);
	}

	@Override
	public LeitoDTO findLeitoByHospedeLeitoId(Long hospedeLeitoId){
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

	public List<Long> hospedagensNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		Condition a1 = HOSPEDE_LEITO.DATA_ENTRADA.between(dataIni, dataFim)
				.or(HOSPEDE_LEITO.DATA_SAIDA.between(dataIni, dataFim));
		Condition a2 = HOSPEDE_LEITO.DATA_ENTRADA.le(dataIni).and(HOSPEDE_LEITO.DATA_SAIDA.ge(dataFim));
		Condition condicao = a1.or(a2);

		List<Long> lista =
		create.select(HOSPEDE.HOSPEDAGEM_ID)
				.from(HOSPEDE_LEITO)
				.innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(HOSPEDE_LEITO.HOSPEDE_ID))
				.where(HOSPEDE_LEITO.ID.eq(leitoId))
				.and(condicao)
				.fetch().getValues(HOSPEDE.HOSPEDAGEM_ID);
		return lista;
	}

	public boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim) {
		Condition a1 = HOSPEDE_LEITO.DATA_ENTRADA.between(dataIni, dataFim)
				.or(HOSPEDE_LEITO.DATA_SAIDA.between(dataIni, dataFim));
		Condition a2 = HOSPEDE_LEITO.DATA_ENTRADA.le(dataIni).and(HOSPEDE_LEITO.DATA_SAIDA.ge(dataFim));
		Condition condicao = a1.or(a2);

		Integer qtd =
				create.select(count())
						.from(HOSPEDE_LEITO)
						.where(HOSPEDE_LEITO.ID.eq(leitoId))
						.and(condicao)
						.fetchOne(0, Integer.class);
		return qtd <= 0;
	}

	public List<Long> hospedagensDePessoaNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim) {
		Condition a1 = HOSPEDE_LEITO.DATA_ENTRADA.between(dataIni, dataFim)
				.or(HOSPEDE_LEITO.DATA_SAIDA.between(dataIni, dataFim));
		Condition a2 = HOSPEDE_LEITO.DATA_ENTRADA.le(dataIni).and(HOSPEDE_LEITO.DATA_SAIDA.ge(dataFim));
		Condition condicao = a1.or(a2);

		List<Long> lista =
				create.select(HOSPEDE.HOSPEDAGEM_ID)
						.from(HOSPEDE_LEITO)
						.innerJoin(HOSPEDE).on(HOSPEDE.ID.eq(HOSPEDE_LEITO.HOSPEDE_ID))
						.where(HOSPEDE.PESSOA_ID.eq(pessoaId))
						.and(condicao)
						.fetch().getValues(HOSPEDE.HOSPEDAGEM_ID);
		return lista;
	}

	public Integer countOfHospedagensParciaisDePessoaNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim) {
		Condition a1 = HOSPEDAGEM.DATA_ENTRADA.between(dataIni, dataFim)
				.or(coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA)
						.between(dataIni, dataFim));
		Condition a2 = HOSPEDAGEM.DATA_ENTRADA.le(dataIni)
				.and(coalesce(HOSPEDAGEM.DATA_EFETIVA_SAIDA, HOSPEDAGEM.DATA_PREVISTA_SAIDA).ge(dataFim));
		Condition a3 = HOSPEDAGEM.TIPO_UTILIZACAO.eq("P");
		Condition condicao = a1.or(a2).and(a3);

		Integer qtd =
				create.select(count())
						.from(HOSPEDAGEM)
						.innerJoin(HOSPEDE).on(HOSPEDE.HOSPEDAGEM_ID.eq(HOSPEDAGEM.ID))
						.where(HOSPEDE.PESSOA_ID.eq(pessoaId))
						.and(condicao)
						.fetchOne(0, Integer.class);
		return qtd;
	}

	@Override
	public void updateDataSaida(Long id, LocalDate dataRenovacao) {
		repository.updateDataSaida(id, dataRenovacao);
	}

}

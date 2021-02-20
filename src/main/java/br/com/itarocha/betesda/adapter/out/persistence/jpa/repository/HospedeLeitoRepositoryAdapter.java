package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.HospedeLeitoMapper;
import br.com.itarocha.betesda.application.out.HospedeLeitoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.HospedeLeito;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
}

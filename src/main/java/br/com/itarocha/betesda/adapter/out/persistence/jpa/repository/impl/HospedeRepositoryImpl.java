package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.HospedeJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.HospedeMapper;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.core.ports.out.HospedeRepository;
import br.com.itarocha.betesda.domain.Hospede;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospedeRepositoryImpl implements HospedeRepository {

	private final HospedeJpaRepository repository;
	private final HospedeMapper mapper;
	private final DSLContext create;

	@Override
	public Hospede save(Hospede model) {
		try {
			return mapper.toModel(repository.save(mapper.toEntity(model)));
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar gravar Hospede"
					, e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public Optional<Hospede> findById(Long id) {
		Optional<HospedeEntity> opt = repository.findById(id);
		return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
	}

	@Override
	public void delete(Hospede model) {
		try {
			repository.delete(mapper.toEntity(model));
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar excluir Hospede"
					, e.getMostSpecificCause().getMessage());
		}
	}

	@Override
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
		} catch ( DataIntegrityViolationException e) {
			throw new IntegridadeException("Falha de integridade ao tentar excluir Hospede"
					, e.getMostSpecificCause().getMessage());
		}
	}
}

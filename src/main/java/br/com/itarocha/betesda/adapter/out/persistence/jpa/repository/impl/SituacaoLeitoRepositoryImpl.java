package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.SituacaoLeitoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.SituacaoLeitoMapper;
import br.com.itarocha.betesda.core.ports.out.SituacaoLeitoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SituacaoLeitoRepositoryImpl implements SituacaoLeitoRepository {

    private final SituacaoLeitoJpaRepository repository;
    private final SituacaoLeitoMapper mapper;

    @Override
    public SituacaoLeito save(SituacaoLeito model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Situação de Leito"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<SituacaoLeito> findById(Long id) {
        Optional<SituacaoLeitoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(SituacaoLeito model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Situação de Leito"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<SituacaoLeito> findAllOrderByDescricao() {
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDictionary> findAllToSelectVO() {
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }
}

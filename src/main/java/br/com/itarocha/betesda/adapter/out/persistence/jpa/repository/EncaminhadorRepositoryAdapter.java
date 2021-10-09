package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.EncaminhadorMapper;
import br.com.itarocha.betesda.core.ports.out.EncaminhadorRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Encaminhador;
import br.com.itarocha.betesda.domain.ItemDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EncaminhadorRepositoryAdapter implements EncaminhadorRepository {

    private final EncaminhadorJpaRepository repository;
    private final EncaminhadorMapper mapper;

    @Override
    public Encaminhador save(Encaminhador model) {
        try {
            EncaminhadorEntity EncaminhadorEntity = mapper.toEntity(model);
            EncaminhadorEntity saved = repository.save(EncaminhadorEntity);
            return mapper.toModel(saved);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Encaminhador"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<Encaminhador> findById(Long id) {
        Optional<EncaminhadorEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Encaminhador model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Encaminhador"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Encaminhador"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<Encaminhador> findAllByEntidadeId(Long entidadeId) {
        return repository.findAllByEntidadeId(entidadeId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDictionary> findAllByEntidadeIdToSelectVO(Long entidadeId) {
        return repository.findAllByEntidadeId(entidadeId)
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }

}
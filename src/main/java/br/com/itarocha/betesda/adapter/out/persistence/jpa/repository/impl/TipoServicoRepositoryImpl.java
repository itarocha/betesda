package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoServicoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoServicoMapper;
import br.com.itarocha.betesda.core.ports.out.TipoServicoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoServicoRepositoryImpl implements TipoServicoRepository {

    private final TipoServicoJpaRepository repository;
    private final TipoServicoMapper mapper;

    @Override
    public TipoServico save(TipoServico model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Tipo de Serviço"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<TipoServico> findById(Long id) {
        Optional<TipoServicoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(TipoServico model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Tipo de Serviço"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<TipoServico> findAllOrderByDescricao() {
        return repository.findAllOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<TipoServico> findAllAtivosOrderByDescricao() {
        return repository.findAllAtivosOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDictionary> findAllToSelectVO() {
        return repository.findAllAtivosOrderByDescricao()
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }
}
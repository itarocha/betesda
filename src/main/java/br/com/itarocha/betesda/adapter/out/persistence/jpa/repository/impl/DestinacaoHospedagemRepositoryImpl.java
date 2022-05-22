package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.DestinacaoHospedagemJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.core.ports.out.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.ItemDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemRepositoryImpl implements DestinacaoHospedagemRepository {

    private final DestinacaoHospedagemJpaRepository repository;
    private final DestinacaoHospedagemMapper mapper;

    @Override
    public DestinacaoHospedagem save(DestinacaoHospedagem model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Destinação de Hospedagem"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<DestinacaoHospedagem> findById(Long id) {
        Optional<DestinacaoHospedagemEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(DestinacaoHospedagem model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Destinação de Hospedagem"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<DestinacaoHospedagem> findAllOrderByDescricao() {
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

package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.EntidadeMapper;
import br.com.itarocha.betesda.core.ports.out.EntidadeRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.domain.ItemDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntidadeRepositoryAdapter implements EntidadeRepository {

    private final EntidadeJpaRepository repository;
    private final EnderecoJpaRepository enderecoRepository;
    private final EntidadeMapper mapper;

    @Override
    public Entidade save(Entidade model) {
        try {
            EntidadeEntity entidadeEntity = mapper.toEntity(model);
            enderecoRepository.save(entidadeEntity.getEndereco());
            EntidadeEntity saved = repository.save(entidadeEntity);
            return mapper.toModel(saved);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Entidade"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<Entidade> findById(Long id) {
        Optional<EntidadeEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Entidade model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Entidade"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Entidade"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<Entidade> findAllOrderByNome() {
        return repository.findAllOrderByNome()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Entidade> findAllLikeNomeLowerCase(String texto) {
        return repository.findAllLikeNomeLowerCase("%"+texto.toLowerCase()+"/%")
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Entidade> existeOutraEntidadeComEsseCNPJ(Long id, String cnpj) {
        return repository.existeOutraEntidadeComEsseCNPJ(id, cnpj)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDictionary> findAllToSelectVO() {
        return repository.findAllOrderByNome()
                .stream()
                .map(mapper::toModel)
                .map(mapper::toSelectValueVO)
                .collect(Collectors.toList());
    }

}
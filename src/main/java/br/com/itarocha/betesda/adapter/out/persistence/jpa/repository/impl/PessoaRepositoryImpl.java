package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.PessoaEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.EnderecoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.PessoaJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.PessoaMapper;
import br.com.itarocha.betesda.core.ports.out.PessoaRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaRepositoryImpl implements PessoaRepository {

    private final PessoaJpaRepository repository;
    private final EnderecoJpaRepository enderecoRepository;
    private final PessoaMapper mapper;

    @Override
    public Pessoa save(Pessoa model) {
        try {
            PessoaEntity PessoaEntity = mapper.toEntity(model);
            enderecoRepository.save(PessoaEntity.getEndereco());
            PessoaEntity saved = repository.save(PessoaEntity);
            return mapper.toModel(saved);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Pessoa"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<Pessoa> findById(Long id) {
        Optional<PessoaEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Pessoa model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch (DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Pessoa"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Pessoa"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<Pessoa> findAllOrderByNome() {
        return repository.findAllOrderByNome()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pessoa> findAllLikeNomeLowerCase(String texto) {
        return repository.findAllLikeNomeLowerCase("%"+texto.toLowerCase()+"%")
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pessoa> getListaOutrosComCpf(Long id, String cpf) {
        if (Objects.isNull(cpf) || "".equals(cpf)) {
            return new ArrayList<>();
        }

        return repository.getListaOutrosComCpf(id, cpf)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pessoa> getListaOutrosComRg(Long id, String rg) {
        if (Objects.isNull(rg) || "".equals(rg)) {
            return new ArrayList<>();
        }

        return repository.getListaOutrosComRg(id, rg)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pessoa> getListaOutrosComCartaoSus(Long id, String cartaoSus) {
        if (Objects.isNull(cartaoSus) || "".equals(cartaoSus)) {
            return new ArrayList<>();
        }

        return repository.getListaOutrosComCartaoSus(id, cartaoSus)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

 }
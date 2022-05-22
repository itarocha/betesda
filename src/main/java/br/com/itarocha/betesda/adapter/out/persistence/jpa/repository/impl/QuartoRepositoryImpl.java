package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.QuartoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.QuartoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.QuartoMapper;
import br.com.itarocha.betesda.core.ports.out.QuartoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Quarto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuartoRepositoryImpl implements QuartoRepository {

    private final QuartoJpaRepository repository;
    private final QuartoMapper mapper;

    @Override
    public Quarto save(Quarto model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Quarto"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<Quarto> findById(Long id) {
        Optional<QuartoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Quarto model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Quarto"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Quarto"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public List<Quarto> findAllOrderByQuartoNumero(){
        return repository.findAllOrderByQuartoNumero()
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> existeOutroQuartoComEsseNumero(Long quartoId, Integer quartoNumero){
        return repository.existeOutroQuartoComEsseNumero(quartoId, quartoNumero)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> existeOutroQuartoComEsseNumero(Integer numero){
        return repository.existeOutroQuartoComEsseNumero(numero)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Quarto> findByDestinacaoHospedagemId(Long quartoId){
        return repository.findByDestinacaoHospedagemId(quartoId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }
}

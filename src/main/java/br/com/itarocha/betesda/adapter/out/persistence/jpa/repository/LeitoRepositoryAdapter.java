package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.LeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.LeitoMapper;
import br.com.itarocha.betesda.application.out.LeitoRepository;
import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeitoRepositoryAdapter implements LeitoRepository {

    private final LeitoJpaRepository repository;
    private final LeitoMapper mapper;

    @Override
    public Leito save(Leito model) {
        return mapper.toModel(repository.save(mapper.toEntity(model)));
    }

    @Override
    public Optional<Leito> findById(Long id) {
        Optional<LeitoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Leito model) {
        repository.delete(mapper.toEntity(model));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Leito> findByQuartoId(Long quartoId){
        return repository.findByQuartoId(quartoId)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWhereQuartoId(Long quartoId){
        repository.deleteWhereQuartoId(quartoId);
    }

    @Override
    public List<Leito> findAllWhereDisponivel(LogicoEnum disponivel) {
        return repository.findAllWhereDisponivel(disponivel)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Leito> existeOutroLeitoComEsseNumero(Long leitoId, Long quartoId, Integer leitoNumero){
        return repository.existeOutroLeitoComEsseNumero(leitoId, quartoId, leitoNumero)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Leito> existeOutroLeitoComEsseNumero(Long quartoId, Integer leitoNumero){
        return repository.existeOutroLeitoComEsseNumero(quartoId, leitoNumero)
                .stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

}

package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.LeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.LeitoMapper;
import br.com.itarocha.betesda.core.ports.out.LeitoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.LeitoDTO;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.itarocha.betesda.jooq.model.Tables.LEITO;
import static br.com.itarocha.betesda.jooq.model.Tables.QUARTO;

@Service
@RequiredArgsConstructor
public class LeitoRepositoryAdapter implements LeitoRepository {

    private final LeitoJpaRepository repository;
    private final LeitoMapper mapper;
    private final DSLContext create;

    @Override
    public Leito save(Leito model) {
        try {
            return mapper.toModel(repository.save(mapper.toEntity(model)));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar gravar Leito"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public Optional<Leito> findById(Long id) {
        Optional<LeitoEntity> opt = repository.findById(id);
        return opt.isPresent() ? Optional.of(mapper.toModel(opt.get())) : Optional.ofNullable(null);
    }

    @Override
    public void delete(Leito model) {
        try {
            repository.delete(mapper.toEntity(model));
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Leito"
                    , e.getMostSpecificCause().getMessage());
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            repository.deleteById(id);
        } catch ( DataIntegrityViolationException e) {
            throw new IntegridadeException("Falha de integridade ao tentar excluir Leito"
                    , e.getMostSpecificCause().getMessage());
        }
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

    @Override
    public List<LeitoDTO> getListaCabecalhosLeitos(){
        return create.select(LEITO.ID, LEITO.NUMERO, QUARTO.ID, QUARTO.NUMERO)
                .from(LEITO)
                .innerJoin(QUARTO).on(LEITO.QUARTO_ID.eq(QUARTO.ID))
                .orderBy(QUARTO.NUMERO, LEITO.NUMERO)
                .fetch()
                .map(r -> new LeitoDTO(r.get(LEITO.ID),r.get(LEITO.NUMERO),r.get(QUARTO.ID),r.get(QUARTO.NUMERO)));
    }

}
package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.impl;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.LeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.LeitoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.LeitoMapper;
import br.com.itarocha.betesda.core.ports.out.LeitoRepository;
import br.com.itarocha.betesda.core.exceptions.IntegridadeException;
import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.LeitoDTO;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import static br.com.itarocha.betesda.jooq.model.Tables.LEITO;
//import static br.com.itarocha.betesda.jooq.model.Tables.QUARTO;
import static org.jooq.impl.DSL.field;
//import static org.jooq.impl.DSL.table;

@Service
@RequiredArgsConstructor
public class LeitoRepositoryImpl implements LeitoRepository {

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
        Field<Long> LEITO_ID = field("LEITO.ID", Long.class);
        Field<Long> QUARTO_ID = field("QUARTO.ID", Long.class);
        Field<Integer> LEITO_NUMERO = field("LEITO.NUMERO", Integer.class);
        Field<Integer> QUARTO_NUMERO = field("QUARTO.NUMERO", Integer.class);

        return create.select(LEITO_ID, LEITO_NUMERO, QUARTO_ID, QUARTO_NUMERO)
                .from("LEITO LEITO")
                .join("QUARTO QUARTO")
                .on("QUARTO.ID = LEITO.QUARTO_ID")
                .orderBy(QUARTO_NUMERO, LEITO_NUMERO)
                .fetch()
                .map(r -> new LeitoDTO(r.get(LEITO_ID),
                        r.get(LEITO_NUMERO),
                        r.get(QUARTO_ID),
                        r.get(QUARTO_NUMERO)));
        /*
        return create.select(LEITO.ID, LEITO.NUMERO, QUARTO.ID, QUARTO.NUMERO)
                .from(LEITO)
                .innerJoin(QUARTO).on(LEITO.QUARTO_ID.eq(QUARTO.ID))
                .orderBy(QUARTO.NUMERO, LEITO.NUMERO)
                .fetch()
                .map(r -> new LeitoDTO(r.get(LEITO.ID),r.get(LEITO.NUMERO),r.get(QUARTO.ID),r.get(QUARTO.NUMERO)));

         */
    }

}
/*
// You can use your table aliases in plain SQL fields
// As long as that will produce syntactically correct SQL
Field<?> LAST_NAME    = field("a.LAST_NAME");

// You can alias your plain SQL fields
Field<?> COUNT1       = field("count(*) x");

// If you know a reasonable Java type for your field, you
// can also provide jOOQ with that type
Field<Integer> COUNT2 = field("count(*) y", Integer.class);

       // Use plain SQL as select fields
create.select(LAST_NAME, COUNT1, COUNT2)

       // Use plain SQL as aliased tables (be aware of syntax!)
      .from("author a")
      .join("book b")

       // Use plain SQL for conditions both in JOIN and WHERE clauses
      .on("a.id = b.author_id")

       // Bind a variable in plain SQL
      .where("b.title != ?", "Brida")

       // Use plain SQL again as fields in GROUP BY and ORDER BY clauses
      .groupBy(LAST_NAME)
      .orderBy(LAST_NAME)
      .fetch();


Query query = create.select(field("BOOK.TITLE"), field("AUTHOR.FIRST_NAME"), field("AUTHOR.LAST_NAME"))
                    .from(table("BOOK"))
                    .join(table("AUTHOR"))
                    .on(field("BOOK.AUTHOR_ID").eq(field("AUTHOR.ID")))
                    .where(field("BOOK.PUBLISHED_IN").eq(1948));
String sql = query.getSQL();
List<Object> bindValues = query.getBindValues();

 */

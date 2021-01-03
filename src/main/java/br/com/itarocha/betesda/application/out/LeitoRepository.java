package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;

import java.util.List;
import java.util.Optional;

public interface LeitoRepository {

    Leito save(Leito model);

    Optional<Leito> findById(Long id);

    void delete(Leito model);

    void deleteById(Long id);

    List<Leito> findByQuartoId(Long id);

    void deleteWhereQuartoId(Long quartoId);

    List<Leito> findAllWhereDisponivel(LogicoEnum disponivel);
}

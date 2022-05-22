package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.LeitoDTO;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;

import java.util.Collection;
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
    Collection<Leito> existeOutroLeitoComEsseNumero(Long leitoId, Long quartoId, Integer leitoNumero);
    Collection<Leito> existeOutroLeitoComEsseNumero(Long quartoId, Integer leitoNumero);
    List<LeitoDTO> getListaCabecalhosLeitos();
}

package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.Leito;
import br.com.itarocha.betesda.domain.Quarto;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface QuartoRepository {

    Quarto save(Quarto model);

    Optional<Quarto> findById(Long id);

    void delete(Quarto model);

    void deleteById(Long id);

    Collection<Leito> existeOutroLeitoComEsseNumero(Long leitoId, Long quartoId, Integer leitoNumero);

    Collection<Leito> existeOutroLeitoComEsseNumero(Long quartoId, Integer leitoNumero);

    List<Quarto> findAllOrderByQuartoNumero();

    List<Quarto> existeOutroQuartoComEsseNumero(Long quartoId, Integer quartoNumero);

    List<Quarto> existeOutroQuartoComEsseNumero(Integer numero);

    List<Quarto> findByDestinacaoHospedagemId(Long quartoId);

}

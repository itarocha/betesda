package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.*;
import java.util.List;

public interface QuartoUseCase {
    Quarto create(Quarto model);
    Quarto create(QuartoNew model) throws Exception;
    Quarto find(Long id);
    Leito findLeito(Long id);
    Leito saveLeito(EditLeitoVO model) throws Exception;
    void remove(Long id);
    void removeLeito(Long id);
    Quarto update(QuartoEdit model);
    List<Quarto> findAll();
    List<Quarto> findAllByDestinacaoHospedagem(Long id);
    List<Leito> findLeitosByQuarto(Long quartoId);
    List<Leito> findLeitosDisponiveis();
    List<ItemDictionary> listTipoLeito();
    boolean existeOutroLeitoComEsseNumero(Long leitoId, Long quartoId, Integer numero);
    boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero);
    boolean existeOutroQuartoComEsseNumero(Long id, Integer numero);
    boolean existeOutroQuartoComEsseNumero(Integer numero);
}

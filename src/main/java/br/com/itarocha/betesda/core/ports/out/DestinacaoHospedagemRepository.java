package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.ItemDictionary;

import java.util.List;
import java.util.Optional;

public interface DestinacaoHospedagemRepository {

    DestinacaoHospedagem save(DestinacaoHospedagem model);

    Optional<DestinacaoHospedagem> findById(Long id);

    void delete(DestinacaoHospedagem model);

    List<DestinacaoHospedagem> findAllOrderByDescricao();

    List<ItemDictionary> findAllToSelectVO();
}

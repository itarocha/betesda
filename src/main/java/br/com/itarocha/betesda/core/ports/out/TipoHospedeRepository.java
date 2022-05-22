package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoHospede;

import java.util.List;
import java.util.Optional;

public interface TipoHospedeRepository {

    TipoHospede save(TipoHospede model);

    Optional<TipoHospede> findById(Long id);

    void delete(TipoHospede model);

    List<TipoHospede> findAllOrderByDescricao();

    List<ItemDictionary> findAllToSelectVO();
}

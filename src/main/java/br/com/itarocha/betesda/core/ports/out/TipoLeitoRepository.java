package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoLeito;

import java.util.List;
import java.util.Optional;

public interface TipoLeitoRepository {

    TipoLeito save(TipoLeito model);

    Optional<TipoLeito> findById(Long id);

    void delete(TipoLeito model);

    List<TipoLeito> findAllOrderByDescricao();

    List<ItemDictionary> findAllToSelectVO();
}

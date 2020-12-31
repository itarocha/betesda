package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoLeito;

import java.util.List;
import java.util.Optional;

public interface TipoLeitoRepository {

    TipoLeito save(TipoLeito model);

    Optional<TipoLeito> findById(Long id);

    void delete(TipoLeito model);

    List<TipoLeito> findAllOrderByDescricao();

    List<SelectValueVO> findAllToSelectVO();
}

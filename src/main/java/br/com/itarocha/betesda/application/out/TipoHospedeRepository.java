package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoHospede;

import java.util.List;
import java.util.Optional;

public interface TipoHospedeRepository {

    TipoHospede save(TipoHospede model);

    Optional<TipoHospede> findById(Long id);

    void delete(TipoHospede model);

    List<TipoHospede> findAllOrderByDescricao();

    List<SelectValueVO> findAllToSelectVO();
}

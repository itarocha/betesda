package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoServico;

import java.util.List;
import java.util.Optional;

public interface TipoServicoRepository {

    TipoServico save(TipoServico model);

    Optional<TipoServico> findById(Long id);

    void delete(TipoServico model);

    List<TipoServico> findAllOrderByDescricao();

    List<TipoServico> findAllAtivosOrderByDescricao();

    List<SelectValueVO> findAllToSelectVO();
}

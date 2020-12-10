package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;

import java.util.List;
import java.util.Optional;

public interface DestinacaoHospedagemRepository {

    DestinacaoHospedagem save(DestinacaoHospedagem model);

    Optional<DestinacaoHospedagem> findById(Long id);

    void delete(DestinacaoHospedagem model);

    List<DestinacaoHospedagem> findAllOrderByDescricao();

    List<SelectValueVO> findAllToSelectVO();
}

package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.Encaminhador;

import java.util.List;
import java.util.Optional;

public interface EncaminhadorRepository {

    Encaminhador save(Encaminhador model);

    Optional<Encaminhador> findById(Long id);

    void delete(Encaminhador model);

    void deleteById(Long id);

    List<Encaminhador> findAllByEntidadeId(Long entidadeId);

    List<SelectValueVO> findAllByEntidadeIdToSelectVO(Long entidadeId);

}

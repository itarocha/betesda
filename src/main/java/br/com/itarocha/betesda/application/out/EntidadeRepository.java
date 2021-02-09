package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.domain.SelectValueVO;

import java.util.List;
import java.util.Optional;

public interface EntidadeRepository {

    Entidade save(Entidade model);

    Optional<Entidade> findById(Long id);

    void delete(Entidade model);

    void deleteById(Long id);

    List<Entidade> findAllOrderByNome();

    List<Entidade> findAllLikeNomeLowerCase(String texto);

    List<Entidade> existeOutraEntidadeComEsseCNPJ(Long id, String cnpj);

    List<SelectValueVO> findAllToSelectVO();
}

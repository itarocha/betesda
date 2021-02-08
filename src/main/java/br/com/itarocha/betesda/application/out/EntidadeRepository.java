package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.domain.SelectValueVO;
import org.springframework.data.repository.query.Param;

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




    /*
    List<Entidade> findByQuartoId(Long id);

    void deleteWhereQuartoId(Long quartoId);

    List<Entidade> findAllWhereDisponivel(LogicoEnum disponivel);

    Collection<Entidade> existeOutroEntidadeComEsseNumero(Long EntidadeId, Long quartoId, Integer EntidadeNumero);

    Collection<Entidade> existeOutroEntidadeComEsseNumero(Long quartoId, Integer EntidadeNumero);
     */
}

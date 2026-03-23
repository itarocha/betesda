package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EncaminhadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EncaminhadorJPARepository extends JpaRepository<EncaminhadorEntity, Long> {

    @Query("SELECT model FROM EncaminhadorEntity model " +
            "WHERE model.entidade.id = :entidadeId AND model.ativo = 'S' " +
            "ORDER BY model.nome")
    List<EncaminhadorEntity> findAllByEntidadeId(Long entidadeId);

}

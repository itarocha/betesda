package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EncaminhadorJpaRepository extends JpaRepository<EncaminhadorEntity, Long> {

    @Query("SELECT model FROM EncaminhadorEntity model WHERE model.entidade.id = :entidadeId ORDER BY model.nome")
    List<EncaminhadorEntity> findAllByEntidadeId(@Param("entidadeId") Long entidadeId);

}

package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoHospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoHospedeJpaRepository extends JpaRepository<TipoHospedeEntity, Long> {

    @Query("SELECT o FROM TipoHospedeEntity o ORDER BY o.descricao")
    List<TipoHospedeEntity> findAllOrderByDescricao();

}

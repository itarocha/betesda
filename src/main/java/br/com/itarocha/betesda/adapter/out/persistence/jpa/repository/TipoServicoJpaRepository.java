package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoServicoJpaRepository extends JpaRepository<TipoServicoEntity, Long> {

    @Query("SELECT e FROM TipoServicoEntity e ORDER BY e.descricao")
    List<TipoServicoEntity> findAllOrderByDescricao();


    @Query("SELECT o FROM TipoServicoEntity o WHERE o.ativo = 'S' ORDER BY o.descricao")
    List<TipoServicoEntity> findAllAtivosOrderByDescricao();
}

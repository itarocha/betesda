package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.SituacaoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SituacaoLeitoJpaRepository extends JpaRepository<SituacaoLeitoEntity, Long> {

	@Query("SELECT e FROM SituacaoLeitoEntity e ORDER BY e.descricao")
	List<SituacaoLeitoEntity> findAllOrderByDescricao();

}

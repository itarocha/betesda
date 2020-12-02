package br.com.itarocha.betesda.adapter.out.persistence.repository;

import br.com.itarocha.betesda.adapter.out.persistence.entity.SituacaoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SituacaoLeitoRepository extends JpaRepository<SituacaoLeitoEntity, Long> {

	@Query("SELECT e FROM SituacaoLeitoEntity e ORDER BY e.descricao")
	List<SituacaoLeitoEntity> findAllOrderByDescricao();

}

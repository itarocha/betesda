package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.SituacaoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SituacaoLeitoEntityRepository extends JpaRepository<SituacaoLeitoEntity, Long> {

	@Query("SELECT e FROM SituacaoLeitoEntity e ORDER BY e.descricao")
	List<SituacaoLeitoEntity> findAllOrderByDescricao();

}

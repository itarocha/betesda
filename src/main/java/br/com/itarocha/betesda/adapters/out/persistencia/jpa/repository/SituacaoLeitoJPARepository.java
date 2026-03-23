package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.SituacaoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SituacaoLeitoJPARepository extends JpaRepository<SituacaoLeitoEntity, Long> {

	@Query("SELECT e FROM SituacaoLeitoEntity e ORDER BY e.descricao")
	List<SituacaoLeitoEntity> findAllOrderByDescricao();

}

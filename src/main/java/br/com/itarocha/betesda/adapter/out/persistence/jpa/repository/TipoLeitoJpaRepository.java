package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoLeitoJpaRepository extends JpaRepository<TipoLeitoEntity, Long> {

	@Query("SELECT o FROM TipoLeitoEntity o ORDER BY o.descricao")
	List<TipoLeitoEntity> findAllOrderByDescricao();

}

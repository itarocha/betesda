package br.com.itarocha.betesda.adapter.out.persistence.repository;

import br.com.itarocha.betesda.adapter.out.persistence.entity.TipoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoLeitoRepository extends JpaRepository<TipoLeitoEntity, Long> {

	
	@Query("SELECT o FROM TipoLeitoEntity o ORDER BY o.descricao")
	List<TipoLeitoEntity> findAllOrderByDescricao();

}
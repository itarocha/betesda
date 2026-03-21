package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.TipoLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoLeitoEntityRepository extends JpaRepository<TipoLeitoEntity, Long> {

	
	@Query("SELECT o FROM TipoLeitoEntity o ORDER BY o.descricao")
	List<TipoLeitoEntity> findAllOrderByDescricao();

}

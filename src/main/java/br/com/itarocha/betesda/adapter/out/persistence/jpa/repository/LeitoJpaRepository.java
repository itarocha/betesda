package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.LeitoEntity;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeitoJpaRepository extends JpaRepository<LeitoEntity, Long> {
	
	List<LeitoEntity> findByQuartoId(Long id);

	@Modifying
	@Query(	"DELETE FROM LeitoEntity o WHERE o.quarto.id = :quartoId")
	void deleteWhereQuartoId(@Param("quartoId") Long id);

	@Query("SELECT e FROM LeitoEntity e WHERE e.situacao.disponivel = :disponivel ORDER BY e.quarto.numero, e.numero")
	List<LeitoEntity> findAllWhereDisponivel(@Param("disponivel") LogicoEnum s);

}

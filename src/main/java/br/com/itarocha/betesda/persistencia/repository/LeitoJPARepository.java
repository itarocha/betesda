package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.LeitoEntity;
import br.com.itarocha.betesda.model.Logico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeitoJPARepository extends JpaRepository<LeitoEntity, Long> {
	
	List<LeitoEntity> findByQuartoId(Long id);

	@Modifying
	@Query(	"DELETE FROM LeitoEntity o WHERE o.quarto.id = :quartoId")
	void deleteWhereQuartoId(@Param("quartoId") Long id);

	
	@Query("SELECT e FROM LeitoEntity e WHERE e.situacao.disponivel = :disponivel ORDER BY e.quarto.numero, e.numero")
	List<LeitoEntity> findAllWhereDisponivel(@Param("disponivel") Logico s);

}

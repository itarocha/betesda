package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.Logico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeitoRepository extends JpaRepository<Leito, Long> {
	
	List<Leito> findByQuartoId(Long id);

	@Modifying
	@Query(	"DELETE FROM Leito o WHERE o.quarto.id = :quartoId")
	void deleteWhereQuartoId(@Param("quartoId") Long id);

	
	@Query("SELECT e FROM Leito e WHERE e.situacao.disponivel = :disponivel ORDER BY e.quarto.numero, e.numero")
	List<Leito> findAllWhereDisponivel(@Param("disponivel") Logico s);

}

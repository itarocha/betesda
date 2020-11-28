package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.SituacaoLeito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SituacaoLeitoRepository extends JpaRepository<SituacaoLeito, Long> {

	@Query("SELECT e FROM SituacaoLeito e ORDER BY e.descricao")
	List<SituacaoLeito> findAllOrderByDescricao();

}

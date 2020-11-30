package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.domain.TipoLeito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TipoLeitoRepository extends JpaRepository<TipoLeito, Long> {

	
	@Query("SELECT o FROM TipoLeito o ORDER BY o.descricao")
	List<TipoLeito> findAllOrderByDescricao();

}

package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.domain.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoServicoRepository extends JpaRepository<TipoServico, Long> {

}

package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.TipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoServicoJPARepository extends JpaRepository<TipoServicoEntity, Long> {

}

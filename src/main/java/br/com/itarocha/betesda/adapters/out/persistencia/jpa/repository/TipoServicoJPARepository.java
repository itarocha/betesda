package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoServicoJPARepository extends JpaRepository<TipoServicoEntity, Long> {

}

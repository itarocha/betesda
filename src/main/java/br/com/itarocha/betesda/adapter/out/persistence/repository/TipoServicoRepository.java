package br.com.itarocha.betesda.adapter.out.persistence.repository;

import br.com.itarocha.betesda.adapter.out.persistence.entity.TipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoServicoRepository extends JpaRepository<TipoServicoEntity, Long> {

}

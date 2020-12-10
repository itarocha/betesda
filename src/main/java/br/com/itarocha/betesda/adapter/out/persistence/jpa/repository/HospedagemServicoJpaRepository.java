package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedagemTipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedagemServicoJpaRepository extends JpaRepository<HospedagemTipoServicoEntity, Long> {

}

package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.HospedagemTipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedagemServicoJPARepository extends JpaRepository<HospedagemTipoServicoEntity, Long> {

}

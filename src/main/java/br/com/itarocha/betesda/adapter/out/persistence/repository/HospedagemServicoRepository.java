package br.com.itarocha.betesda.adapter.out.persistence.repository;

import br.com.itarocha.betesda.adapter.out.persistence.entity.HospedagemTipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedagemServicoRepository extends JpaRepository<HospedagemTipoServicoEntity, Long> {

}

package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.model.HospedagemTipoServicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedagemServicoEntityRepository extends JpaRepository<HospedagemTipoServicoEntity, Long> {

}

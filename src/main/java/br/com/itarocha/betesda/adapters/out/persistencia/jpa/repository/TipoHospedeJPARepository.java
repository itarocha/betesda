package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoHospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoHospedeJPARepository extends JpaRepository<TipoHospedeEntity, Long> {

}

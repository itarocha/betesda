package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.TipoHospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoHospedeJPARepository extends JpaRepository<TipoHospedeEntity, Long> {

}

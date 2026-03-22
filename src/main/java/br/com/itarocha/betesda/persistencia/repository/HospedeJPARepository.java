package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.HospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedeJPARepository extends JpaRepository<HospedeEntity, Long> {

}

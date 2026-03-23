package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.HospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedeJPARepository extends JpaRepository<HospedeEntity, Long> {

}

package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospedeJpaRepository extends JpaRepository<HospedeEntity, Long> {

}

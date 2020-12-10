package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncaminhadorJpaRepository extends JpaRepository<EncaminhadorEntity, Long> {

}

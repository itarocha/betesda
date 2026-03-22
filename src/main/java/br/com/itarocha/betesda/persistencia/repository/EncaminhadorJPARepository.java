package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.EncaminhadorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EncaminhadorJPARepository extends JpaRepository<EncaminhadorEntity, Long> {

}

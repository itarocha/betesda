package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.EntidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntidadeEntityRepository extends JpaRepository<EntidadeEntity, Long> {

}

package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EntidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntidadeJPARepository extends JpaRepository<EntidadeEntity, Long> {

}

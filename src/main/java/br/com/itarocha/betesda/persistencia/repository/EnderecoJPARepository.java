package br.com.itarocha.betesda.persistencia.repository;

import br.com.itarocha.betesda.persistencia.model.EnderecoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoJPARepository extends JpaRepository<EnderecoEntity, Long> {

}

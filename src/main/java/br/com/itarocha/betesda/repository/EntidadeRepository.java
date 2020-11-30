package br.com.itarocha.betesda.repository;

import br.com.itarocha.betesda.domain.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntidadeRepository extends JpaRepository<Entidade, Long> {

}

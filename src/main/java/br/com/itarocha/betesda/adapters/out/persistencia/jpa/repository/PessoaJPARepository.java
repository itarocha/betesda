package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PessoaJPARepository extends JpaRepository<PessoaEntity, Long>, JpaSpecificationExecutor<PessoaEntity> {

}

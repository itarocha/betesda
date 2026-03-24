package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EntidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntidadeJPARepository extends JpaRepository<EntidadeEntity, Long> {

    @Query("SELECT model FROM EntidadeEntity model ORDER BY model.nome")
    List<EntidadeEntity> findAll();

}

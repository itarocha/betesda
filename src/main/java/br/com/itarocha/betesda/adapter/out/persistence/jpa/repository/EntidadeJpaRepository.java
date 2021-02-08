package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.QuartoEntity;
import br.com.itarocha.betesda.domain.Entidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntidadeJpaRepository extends JpaRepository<EntidadeEntity, Long> {

    @Query("SELECT e FROM EntidadeEntity e ORDER BY e.nome")
    List<EntidadeEntity> findAllOrderByNome();

    @Query("SELECT model FROM EntidadeEntity model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome")
    List<EntidadeEntity> findAllLikeNomeLowerCase(@Param("texto") String texto);

    @Query("SELECT model FROM EntidadeEntity model WHERE (model.id <> :id) AND (model.cnpj = :cnpj)")
    List<EntidadeEntity> existeOutraEntidadeComEsseCNPJ(@Param("id") Long id, @Param("cnpj") String cnpj);
}

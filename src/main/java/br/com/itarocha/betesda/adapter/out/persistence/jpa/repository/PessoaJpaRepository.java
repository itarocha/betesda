package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.PessoaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PessoaJpaRepository extends JpaRepository<PessoaEntity, Long>, JpaSpecificationExecutor<PessoaEntity> {

    @Query("SELECT model FROM PessoaEntity model ORDER BY model.nome")
    List<PessoaEntity> findAllOrderByNome();

    @Query("SELECT model FROM PessoaEntity model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome")
    List<PessoaEntity> findAllLikeNomeLowerCase(@Param("texto") String texto);

    @Query("SELECT model FROM PessoaEntity model WHERE (model.id <> :id) AND (model.cpf = :cpf)")
    List<PessoaEntity> getListaOutrosComCpf(@Param("id") Long id, @Param("cpf") String cpf);

    @Query("SELECT model FROM PessoaEntity model WHERE (model.id <> :id) AND (model.rg = :rg)")
    List<PessoaEntity> getListaOutrosComRg(@Param("id") Long id, @Param("rg") String rg);

    @Query("SELECT model FROM PessoaEntity model WHERE (model.id <> :id) AND (model.cartaoSus = :cartaoSus)")
    List<PessoaEntity> getListaOutrosComCartaoSus(@Param("id") Long id, @Param("cartaoSus") String cartaoSus);

}

package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.QuartoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuartoJpaRepository extends JpaRepository<QuartoEntity, Long> {
	
	 //@Query("SELECT q FROM Quarto q FETCH ALL PROPERTIES ORDER BY q.numero")
	 @Query("SELECT q FROM QuartoEntity q ORDER BY q.numero")
	 List<QuartoEntity> findAllOrderByQuartoNumero();

	 @Query("SELECT o FROM QuartoEntity o WHERE (o.id <> :id) AND (o.numero = :numero)")
	 List<QuartoEntity> existeOutroQuartoComEsseNumero(@Param("id") Long id, @Param("numero") Integer numero);

	 @Query("SELECT o FROM QuartoEntity o WHERE (o.numero = :numero)")
	 List<QuartoEntity> existeOutroQuartoComEsseNumero(@Param("numero") Integer numero);

	 @Query("SELECT q FROM QuartoEntity q JOIN q.destinacoes d FETCH ALL PROPERTIES WHERE d.id = :id ORDER BY q.numero") // JOIN FETCH
	 List<QuartoEntity> findByDestinacaoHospedagemId(@Param("id") Long id);
}

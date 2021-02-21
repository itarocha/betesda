package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospedagemJpaRepository extends JpaRepository<HospedagemEntity, Long> {
	
	@Query("SELECT  DISTINCT hospedagem " + 
			"FROM   HospedagemEntity hospedagem FETCH ALL PROPERTIES " +
			"WHERE  EXISTS( " +
			"				SELECT      hospedeLeito " + 			
			"				FROM        HospedeLeitoEntity hospedeLeito " +
			"				INNER JOIN  hospedeLeito.hospede hospede " +
			"				INNER JOIN  hospede.hospedagem hpd " +
			"				WHERE       hpd.id = hospedagem.id " +
			"				AND         hospedeLeito.id = :hospedeLeitoId " +
			") ")
	HospedagemEntity findHospedagemByHospedeLeitoId(@Param("hospedeLeitoId") Long hospedeLeitoId);
	
	@Query("SELECT  hospedagem " + 
			"FROM   HospedagemEntity hospedagem FETCH ALL PROPERTIES " +
			"WHERE  hospedagem.id = :hospedagemId")
	HospedagemEntity findHospedagemByHospedagemId(@Param("hospedagemId") Long hospedagemId);
}

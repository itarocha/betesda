package br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.HospedagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HospedagemJPARepository extends JpaRepository<HospedagemEntity, Long> {
	
	@Query("SELECT  DISTINCT hospedagem " + 
			"FROM   HospedagemEntity hospedagem " +
			"WHERE  EXISTS( " +
			"				SELECT      hospedeLeito " + 			
			"				FROM        HospedeLeitoEntity hospedeLeito " +
			"				INNER JOIN  hospedeLeito.hospede hospede " +
			"				INNER JOIN  hospede.hospedagem hpd " +
			"				WHERE       hpd.id = hospedagem.id " +
			"				AND         hospedeLeito.id = :hospedeLeitoId " +
			") ")
	public HospedagemEntity findHospedagemByHospedeLeitoId(@Param("hospedeLeitoId") Long hospedeLeitoId);
	
	@Query("SELECT  hospedagem " + 
			"FROM   HospedagemEntity hospedagem " +
			"WHERE  hospedagem.id = :hospedagemId")
	public HospedagemEntity findHospedagemByHospedagemId(@Param("hospedagemId") Long hospedagemId);
}

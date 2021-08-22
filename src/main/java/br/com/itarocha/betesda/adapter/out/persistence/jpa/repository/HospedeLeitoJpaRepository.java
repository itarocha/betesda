package br.com.itarocha.betesda.adapter.out.persistence.jpa.repository;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.HospedeLeitoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

public interface HospedeLeitoJpaRepository extends JpaRepository<HospedeLeitoEntity, Long> {

	@Query(value = "SELECT   hl.* " +
			"FROM     hospede_leito hl " + 
			"WHERE    hl.hospede_id IN (SELECT hpd.id FROM hospede hpd WHERE hpd.hospedagem_id = :hospedagemId) " + 
			"AND      hl.data_entrada = (SELECT MAX(outro.data_entrada) FROM hospede_leito outro WHERE outro.hospede_id = hl.hospede_id) " + 
			"ORDER BY hl.data_entrada DESC", nativeQuery = true) 
	List<HospedeLeitoEntity> findUltimoByHospedagemId(@Param("hospedagemId") Long hospedagemId);
	
	// Retorna só um
	@Query(value = "SELECT   hl.* " + 
	"FROM     hospede_leito hl " + 
	"WHERE    hl.hospede_id = :hospedeId " +
	"AND      hl.data_entrada = (SELECT MAX(outro.data_entrada) FROM hospede_leito outro WHERE outro.hospede_id = hl.hospede_id) " + 
	"ORDER BY hl.data_entrada ", nativeQuery = true)
	List<HospedeLeitoEntity> findUltimoByHospedeId(@Param("hospedeId") Long hospedeId);
	

	// Retorna se existe alguma hospedagem no leito no período
	@Query(value = "SELECT hl.* " +
	"FROM   hospede_leito hl "+
	"WHERE       (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim)) "+
	"OR          ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim))) "+
	"AND         hl.leito_id = :leitoId ", nativeQuery = true)
	List<HospedeLeitoEntity> findByLeitoNoPeriodo(@Param("leitoId") Long leitoId, @Param("dataIni") LocalDate dataIni, @Param("dataFim") LocalDate dataFim);

	
	@Query(value = "SELECT COUNT(*) FROM  hospede h WHERE h.baixado = 'N' AND h.hospedagem_id = :hospedagemId", nativeQuery = true)	
	Long countHospedesNaoBaixadosByHospedagemId(@Param("hospedagemId") Long hospedagemId);

	@Query(value = "SELECT     MAX(hl.data_entrada) data_entrada "+ 
	"FROM       hospede h "+
	"INNER JOIN hospede_leito hl "+
	"ON         hl.hospede_id = h.id "+
	"WHERE      h.hospedagem_id = :hospedagemId ", nativeQuery = true)	
	LocalDate ultimaDataEntradaByHospedagemId(@Param("hospedagemId") Long hospedagemId);
	
	@Query(value = "SELECT     MAX(hl.data_entrada) data_entrada "+ 
	"FROM       hospede h "+
	"INNER JOIN hospede_leito hl "+
	"ON         hl.hospede_id = h.id "+
	"WHERE      h.hospedagem_id = :hospedagemId " +
	"AND        h.id = :hospedeId ", nativeQuery = true)	
	LocalDate ultimaDataEntradaByHospedagemId(@Param("hospedagemId") Long hospedagemId, @Param("hospedeId") Long hospedeId);
	
	@Query(value = 	"SELECT     DISTINCT hl.leito_id "+
			"FROM       hospede_leito hl "+
			"WHERE      (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim)) "+
			"OR          ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim))) ",nativeQuery = true)
	List<BigInteger> leitosNoPeriodo(@Param("dataIni") LocalDate dataIni, @Param("dataFim") LocalDate dataFim);
	
	@Query(value = 	"SELECT     DISTINCT hl.leito_id "+
			"FROM       hospede_leito hl "+
			"INNER JOIN hospede h " + 
			"ON         h.id = hl.hospede_id " + 
			"WHERE      h.hospedagem_id = :hospedagemId " +
			"AND        (((hl.data_entrada BETWEEN :dataIni AND :dataFim) OR (hl.data_saida BETWEEN :dataIni AND :dataFim)) "+
			"OR          ((hl.data_entrada <= :dataIni) AND (hl.data_saida >= :dataFim))) ",nativeQuery = true)
	List<BigInteger> leitosNoPeriodoPorHospedagem(@Param("hospedagemId")Long hospedagemId, @Param("dataIni") LocalDate dataIni, @Param("dataFim") LocalDate dataFim);

	@Modifying
	@Query(value = "update hospede_leito hl set hl.data_saida = :dataRenovacao where hl.id = :id", nativeQuery = true)
	void updateDataSaida(@Param("id") Long id, @Param("dataRenovacao") LocalDate dataRenovacao);
}
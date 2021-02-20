package br.com.itarocha.betesda.application.out;

import br.com.itarocha.betesda.domain.HospedeLeito;
import br.com.itarocha.betesda.domain.Leito;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HospedeLeitoRepository{

	HospedeLeito save(HospedeLeito model);

	Optional<HospedeLeito> findById(Long id);

	void delete(HospedeLeito model);

	void deleteById(Long id);

	//List<Leito> findByQuartoId(Long id);

	List<HospedeLeito> findUltimoByHospedagemId(@Param("hospedagemId") Long hospedagemId);
	
	List<HospedeLeito> findUltimoByHospedeId(@Param("hospedeId") Long hospedeId);
	List<HospedeLeito> findByLeitoNoPeriodo(@Param("leitoId") Long leitoId, @Param("dataIni") LocalDate dataIni, @Param("dataFim") LocalDate dataFim);

	Long countHospedesNaoBaixadosByHospedagemId(@Param("hospedagemId") Long hospedagemId);

	LocalDate ultimaDataEntradaByHospedagemId(@Param("hospedagemId") Long hospedagemId);
	
	LocalDate ultimaDataEntradaByHospedagemId(@Param("hospedagemId") Long hospedagemId, @Param("hospedeId") Long hospedeId);
	
	List<BigInteger> leitosNoPeriodo(@Param("dataIni") LocalDate dataIni, @Param("dataFim") LocalDate dataFim);
	
	List<BigInteger> leitosNoPeriodoPorHospedagem(@Param("hospedagemId")Long hospedagemId, @Param("dataIni") LocalDate dataIni, @Param("dataFim") LocalDate dataFim);
}

package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.domain.Hospedagem;
import br.com.itarocha.betesda.domain.HospedagemNew;

import java.time.LocalDate;
import java.util.Optional;

public interface HospedagemRepository {

	Hospedagem save(Hospedagem model);

	Hospedagem save(HospedagemNew model);

	Optional<Hospedagem> findById(Long id);

	void delete(Hospedagem model);

	void deleteById(Long id);

	Hospedagem findHospedagemByHospedagemId(Long hospedagemId);

	Hospedagem updateDataPrevistaSaida(Long hospedagemId, LocalDate dataRenovacao);

	/*
	List<HospedeLeito> findUltimoByHospedagemId(Long hospedagemId);
	
	List<HospedeLeito> findUltimoByHospedeId(Long hospedeId);

	List<HospedeLeito> findByLeitoNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim);

	Long countHospedesNaoBaixadosByHospedagemId(Long hospedagemId);

	LocalDate ultimaDataEntradaByHospedagemId(Long hospedagemId);
	
	LocalDate ultimaDataEntradaByHospedagemId(Long hospedagemId, Long hospedeId);
	
	List<BigInteger> leitosNoPeriodo(LocalDate dataIni, LocalDate dataFim);
	
	List<BigInteger> leitosNoPeriodoPorHospedagem(Long hospedagemId, LocalDate dataIni, LocalDate dataFim);

	LeitoDTO findLeitoByHospedeLeitoId(Long hospedeLeitoId);

	List<Long> hospedagensNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim);

	boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim);

	List<Long> hospedagensDePessoaNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim);

	Integer countOfHospedagensParciaisDePessoaNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim);

	void updateDataSaida(Long id, LocalDate dataRenovacao);

	 */
}

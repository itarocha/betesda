package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.*;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.hospedagem.*;

import java.time.LocalDate;
import java.util.*;

public interface HospedagemUseCase {

	HospedagemEntity create(HospedagemVO model);

	List<OcupacaoLeito> getLeitosOcupadosNoPeriodo(Long hospedagemId, LocalDate dataIni, LocalDate dataFim);

	HospedagemFullVO getHospedagemPorHospedeLeitoId(Long hospedagemId);

	void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento);

	void baixarHospede(Long hospedeId, LocalDate dataBaixa);

	void removerHospede(Long hospedagemId, Long hospedeId);

	void alterarTipoHospede(Long hospedeId, Long tipoHospedeId);

	void transferirHospede(Long hospedeId, Long leitoId, LocalDate dataTransferencia);

	void adicionarHospede(Long hospedagemId, Long pessoaId, Long tipoHospedeId, Long leitoId, LocalDate dataEntrada);

	void renovarHospedagem(Long hospedagemId, LocalDate dataRenovacao);

	void excluirHospedagem(Long id);

	boolean pessoaLivreNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim);

	boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim);

}
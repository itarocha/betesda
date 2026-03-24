package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.*;
import br.com.itarocha.betesda.core.domain.model.HospedagemFullVO;
import br.com.itarocha.betesda.core.domain.model.HospedagemVO;
import br.com.itarocha.betesda.core.domain.model.hospedagem.*;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.*;

public interface HospedagemPort {

	HospedagemEntity create(HospedagemVO model);
	@Deprecated
	MapaRetorno buildMapaRetorno(LocalDate dataBase);
	MapaLinhas buildMapaLinhas(LocalDate dataBase);
	MapaHospedes buildMapaHospedes(LocalDate dataBase);
	MapaCidades buildMapaCidades(LocalDate dataBase);
	MapaQuadro buildMapaQuadro(LocalDate dataBase);
	List<OcupacaoLeito> getLeitosOcupadosNoPeriodo(Long hospedagemId, LocalDate dataIni, LocalDate dataFim);
	HospedagemFullVO getHospedagemPorHospedeLeitoId(Long hospedagemId);
	void encerrarHospedagem(Long hospedagemId, LocalDate dataEncerramento);
	void baixarHospede(Long hospedeId, LocalDate dataBaixa);
	void removerHospede(Long hospedagemId, Long hospedeId);
	void alterarTipoHospede(Long hospedeId, Long tipoHospedeId);
	void transferirHospede(Long hospedeId, Long leitoId, LocalDate dataTransferencia);
	void adicionarHospede(Long hospedagemId, Long pessoaId, Long tipoHospedeId, Long leitoId, LocalDate dataEntrada);
	void renovarHospedagem(Long hospedagemId, LocalDate dataRenovacao);
	void createNaoAtendimento(Long hospedagemId, LocalDate dataNaoAtendimento);
	void excluirHospedagem(Long id);
	void removeNaoAtendimento(Long hospedagemId, Long naoAtendimentoId);
	boolean pessoaLivreNoPeriodo(Long pessoaId, LocalDate dataIni, LocalDate dataFim);
	boolean leitoLivreNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim);
	List<BigInteger> hospedagensNoPeriodo(Long leitoId, LocalDate dataIni, LocalDate dataFim);

}


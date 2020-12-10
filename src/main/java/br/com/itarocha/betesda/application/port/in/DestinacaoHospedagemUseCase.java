package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;

import java.util.List;

public interface DestinacaoHospedagemUseCase {

	DestinacaoHospedagem create(DestinacaoHospedagem model);

	void remove(Long id);

	DestinacaoHospedagem find(Long id);

	List<DestinacaoHospedagem> findAll();

	List<SelectValueVO> listSelect();

}

package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.core.domain.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;

import java.util.List;

public interface DestinacaoHospedagemPort {

	DestinacaoHospedagem create(DestinacaoHospedagem model);
	void remove(Long id);
	DestinacaoHospedagem find(Long id);
	List<DestinacaoHospedagem> findAll();
	List<ValorTexto> listSelect();

}

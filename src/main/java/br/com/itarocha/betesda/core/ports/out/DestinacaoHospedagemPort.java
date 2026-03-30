package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;

import java.util.List;

public interface DestinacaoHospedagemPort {

	DestinacaoHospedagemEntity create(DestinacaoHospedagemEntity model);
	void remove(Long id);
	DestinacaoHospedagemEntity find(Long id);
	List<DestinacaoHospedagemEntity> findAll();
	List<ValorTexto> listSelect();

}

package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoHospede;

import java.util.List;

public interface TipoHospedeUseCase {
	TipoHospede create(TipoHospede model);
	void remove(Long id);
	TipoHospede update(TipoHospede model);
	TipoHospede find(Long id);
	List<TipoHospede> findAll();
	List<ItemDictionary> listSelect();
}

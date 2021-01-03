package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoHospede;

import java.util.List;

public interface TipoHospedeUseCase {

	TipoHospede create(TipoHospede model);

	void remove(Long id);

	TipoHospede update(TipoHospede model);

	TipoHospede find(Long id);

	List<TipoHospede> findAll();

	List<SelectValueVO> listSelect();
}

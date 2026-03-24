package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoHospedeEntity;
import br.com.itarocha.betesda.core.domain.model.SelectValueVO;

import java.util.List;

public interface TipoHospedePort {

	TipoHospedeEntity create(TipoHospedeEntity model);
	void remove(Long id);
	TipoHospedeEntity update(TipoHospedeEntity model);
	TipoHospedeEntity find(Long id);
	List<TipoHospedeEntity> findAll();
	List<SelectValueVO> listSelect();
	
}

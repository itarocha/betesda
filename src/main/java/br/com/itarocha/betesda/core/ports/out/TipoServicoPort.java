package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;

import java.util.List;

public interface TipoServicoPort {

	TipoServicoEntity create(TipoServicoEntity model);
	void remove(Long id);
	TipoServicoEntity update(TipoServicoEntity model);
  	TipoServicoEntity find(Long id);
	List<TipoServicoEntity> findAll();
	List<ValorTexto> listSelect();
}

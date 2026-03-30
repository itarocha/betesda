package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoLeitoEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;

import java.util.List;

public interface TipoLeitoPort {

	TipoLeitoEntity create(TipoLeitoEntity model);
	void remove(Long id);
	TipoLeitoEntity update(TipoLeitoEntity model);
 	TipoLeitoEntity find(Long id);
	List<TipoLeitoEntity> findAll();
	List<ValorTexto> listSelect();

}

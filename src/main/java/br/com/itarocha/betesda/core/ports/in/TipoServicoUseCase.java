package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoServico;

import java.util.List;

public interface TipoServicoUseCase {
	TipoServico create(TipoServico model);
	void remove(Long id);
	TipoServico update(TipoServico model);
	TipoServico find(Long id);
	List<TipoServico> findAll();
	List<ItemDictionary> listSelect();
}

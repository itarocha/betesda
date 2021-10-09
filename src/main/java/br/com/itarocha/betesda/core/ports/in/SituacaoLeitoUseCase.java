package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.SituacaoLeito;

import java.util.List;

public interface SituacaoLeitoUseCase {
	SituacaoLeito create(SituacaoLeito model);
	void remove(Long id);
	SituacaoLeito find(Long id);
	List<SituacaoLeito> findAll();
	List<ItemDictionary> listSelect();
}

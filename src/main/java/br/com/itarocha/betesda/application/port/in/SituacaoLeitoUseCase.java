package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.SituacaoLeito;

import java.util.List;

public interface SituacaoLeitoUseCase {

	SituacaoLeito create(SituacaoLeito model);

	void remove(Long id);

	SituacaoLeito find(Long id);

	List<SituacaoLeito> findAll();

	List<SelectValueVO> listSelect();

}

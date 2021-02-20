package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.domain.Encaminhador;
import br.com.itarocha.betesda.domain.SelectValueVO;

import java.util.List;
import java.util.Optional;

public interface EncaminhadorUseCase {

	Encaminhador create(Encaminhador model);

	void remove(Long id);

	Encaminhador update(Encaminhador model);

	Optional<Encaminhador> find(Long id);

	List<Encaminhador> findAllByEntidadeId(Long entidadeId);

	List<SelectValueVO> listSelectByEntidadeId(Long entidadeId);
	
}

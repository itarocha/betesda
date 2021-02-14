package br.com.itarocha.betesda.application.port.in;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.domain.SelectValueVO;

import java.util.List;
import java.util.Optional;

public interface EncaminhadorUseCase {

	EncaminhadorEntity create(EncaminhadorEntity model);

	void remove(Long id);

	EncaminhadorEntity update(EncaminhadorEntity model);

	Optional<EncaminhadorEntity> find(Long id);

	List<EncaminhadorEntity> findAll(Long entidadeId);

	List<SelectValueVO> listSelect(Long entidadeId);
	
}

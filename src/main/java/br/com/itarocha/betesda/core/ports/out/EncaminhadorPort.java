package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.core.domain.model.SelectValueVO;

import java.util.List;
import java.util.Optional;

public interface EncaminhadorPort {

	EncaminhadorEntity create(EncaminhadorEntity model);
	void remove(Long id);
	EncaminhadorEntity update(EncaminhadorEntity model);
	Optional<EncaminhadorEntity> find(Long id);
	List<EncaminhadorEntity> findAll(Long entidadeId);
	List<SelectValueVO> listSelect(Long entidadeId);
	
}

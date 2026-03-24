package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.core.domain.model.SelectValueVO;

import java.util.List;

public interface SituacaoLeitoPort {

	SituacaoLeitoEntity create(SituacaoLeitoEntity model);
	void remove(Long id);
	SituacaoLeitoEntity find(Long id);
	List<SituacaoLeitoEntity> findAll();
	List<SelectValueVO> listSelect();

}

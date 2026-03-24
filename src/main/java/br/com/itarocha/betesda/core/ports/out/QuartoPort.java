package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.*;
import br.com.itarocha.betesda.core.domain.model.EditLeitoVO;
import br.com.itarocha.betesda.core.domain.model.EditQuartoVO;
import br.com.itarocha.betesda.core.domain.model.NovoQuartoVO;
import br.com.itarocha.betesda.core.domain.model.SelectValueVO;

import java.util.List;

public interface QuartoPort {

	QuartoEntity create(QuartoEntity model);
	QuartoEntity create(NovoQuartoVO model);
  	QuartoEntity find(Long id);
  	LeitoEntity findLeito(Long id);
  	LeitoEntity saveLeito(EditLeitoVO model);
	void remove(Long id);
	void removeLeito(Long id);
	QuartoEntity update(EditQuartoVO model);
	List<QuartoEntity> findAll();
	List<QuartoEntity> findAllByDestinacaoHospedagem(Long id);
	List<LeitoEntity> findLeitosByQuarto(Long quartoId);
	List<LeitoEntity> findLeitosDisponiveis();
	List<SelectValueVO> listTipoLeito();
	boolean existeOutroLeitoComEsseNumero(Long leito_id, Long quartoId, Integer numero);
	boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero);
	boolean existeOutroQuartoComEsseNumero(Long id, Integer numero);
	boolean existeOutroQuartoComEsseNumero(Integer numero);

}
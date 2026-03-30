package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.*;
import br.com.itarocha.betesda.core.domain.model.LeitoEdicao;
import br.com.itarocha.betesda.core.domain.model.QuartoEdicao;
import br.com.itarocha.betesda.core.domain.model.QuartoNovo;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;

import java.util.List;

public interface QuartoPort {

	QuartoEntity create(QuartoEntity model);
	QuartoEntity create(QuartoNovo model);
  	QuartoEntity find(Long id);
  	LeitoEntity findLeito(Long id);
  	LeitoEntity saveLeito(LeitoEdicao model);
	void remove(Long id);
	void removeLeito(Long id);
	QuartoEntity update(QuartoEdicao model);
	List<QuartoEntity> findAll();
	List<QuartoEntity> findAllByDestinacaoHospedagem(Long id);
	List<LeitoEntity> findLeitosByQuarto(Long quartoId);
	List<LeitoEntity> findLeitosDisponiveis();
	List<ValorTexto> listTipoLeito();
	boolean existeOutroLeitoComEsseNumero(Long leito_id, Long quartoId, Integer numero);
	boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero);
	boolean existeOutroQuartoComEsseNumero(Long id, Integer numero);
	boolean existeOutroQuartoComEsseNumero(Integer numero);

}
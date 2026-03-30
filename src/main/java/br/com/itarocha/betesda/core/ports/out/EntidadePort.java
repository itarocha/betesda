package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;

import java.util.List;
import java.util.Optional;

public interface EntidadePort {

	EntidadeEntity create(EntidadeEntity model);
	void remove(Long id);
	EntidadeEntity update(EntidadeEntity model);
	Optional<EntidadeEntity> find(Long id);
	List<EntidadeEntity> findAll();
	List<EntidadeEntity> consultar(String texto);
	List<ValorTexto> listSelect();
	boolean entidadeCadastradaPorCampo(Long entidadeId, String campo, String valor);
}

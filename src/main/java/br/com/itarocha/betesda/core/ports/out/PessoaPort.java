package br.com.itarocha.betesda.core.ports.out;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.PessoaEntity;
import java.util.List;
import java.util.Optional;

public interface PessoaPort {

	PessoaEntity create(PessoaEntity model);
	void remove(Long id);
	PessoaEntity update(PessoaEntity model);
	Optional<PessoaEntity> find(Long id);
	List<PessoaEntity> findByFieldNameAndValue(String campo, String valor);
	List<PessoaEntity> findAll();
	List<PessoaEntity> consultar(String texto);
	boolean pessoaCadastradaPorCampo(Long pessoaId, String campo, String valor);
	
}

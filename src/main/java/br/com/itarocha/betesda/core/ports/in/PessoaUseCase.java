package br.com.itarocha.betesda.core.ports.in;

import br.com.itarocha.betesda.domain.Pessoa;

import java.util.List;
import java.util.Optional;

public interface PessoaUseCase {
	Pessoa create(Pessoa model);
	void remove(Long id);
	Pessoa update(Pessoa model);
	Optional<Pessoa> find(Long id);
	List<Pessoa> findAll();
	List<Pessoa> findAllLikeNomeLowerCase(String nome);
}

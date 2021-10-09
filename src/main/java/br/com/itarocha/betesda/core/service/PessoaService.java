package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.out.PessoaRepository;
import br.com.itarocha.betesda.core.ports.in.PessoaUseCase;
import br.com.itarocha.betesda.domain.Pessoa;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import br.com.itarocha.betesda.util.validacoes.Violation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PessoaService implements PessoaUseCase {

	private final PessoaRepository repositorio;

	public Pessoa create(Pessoa model){
		try{
			Set<Violation> violations = new HashSet<>();

			Long id = model.getId() == null ? 0L : model.getId();

			if (!repositorio.getListaOutrosComCpf(id, model.getCpf()).isEmpty() ) {
				violations.add(new Violation("cpf", "CPF já casdastrado para outra pessoa"));
			}

			if (!repositorio.getListaOutrosComRg(id, model.getRg()).isEmpty()) {
				violations.add(new Violation("rg", "RG já casdastrado para outra pessoa"));
			}
			if (!repositorio.getListaOutrosComCartaoSus(id, model.getCartaoSus()).isEmpty()) {
				violations.add(new Violation("cartaoSus", "Cartão do SUS já casdastrado para outra pessoa"));
			}

			if (!violations.isEmpty()){
				throw new EntityValidationException(violations);
			}

			repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<Pessoa> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public Pessoa update(Pessoa model) {
		Optional<Pessoa> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<Pessoa> find(Long id) {
		return repositorio.findById(id);
	}

	public List<Pessoa> findAll() {
		return repositorio.findAllOrderByNome();
	}

	@Override
	public List<Pessoa> findAllLikeNomeLowerCase(String nome) {
		return repositorio.findAllLikeNomeLowerCase(nome);
	}

}

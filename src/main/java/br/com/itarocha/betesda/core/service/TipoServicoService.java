package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.out.TipoServicoRepository;
import br.com.itarocha.betesda.core.ports.in.TipoServicoUseCase;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoServicoService implements TipoServicoUseCase {

	private final TipoServicoRepository repository;

	public TipoServico create(TipoServico model) {
		try{
			return repository.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	public TipoServico update(TipoServico model) {
		Optional<TipoServico> result = repository.findById(model.getId());
		return result.isPresent() ? repository.save(result.get()) : null;
	}

  	public TipoServico find(Long id) {
		Optional<TipoServico> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	public List<TipoServico> findAll() {
		return repository.findAllOrderByDescricao();
	}
	
	public List<ItemDictionary> listSelect() {
		return repository.findAllToSelectVO();
	}
}

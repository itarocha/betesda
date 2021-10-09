package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.out.TipoLeitoRepository;
import br.com.itarocha.betesda.core.ports.in.TipoLeitoUseCase;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.domain.TipoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoLeitoService implements TipoLeitoUseCase {

	private final TipoLeitoRepository repository;

	@Override
	public TipoLeito create(TipoLeito model) {
		return repository.save(model);
	}

	@Override
	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	@Override
	public TipoLeito update(TipoLeito model) {
		Optional<TipoLeito> result = repository.findById(model.getId());
		return result.isPresent() ? repository.save(result.get()) : null;
	}

	@Override
  	public TipoLeito find(Long id) {
		Optional<TipoLeito> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public List<TipoLeito> findAll() {
		return repository.findAllOrderByDescricao();
	}

	@Override
	public List<ItemDictionary> listSelect() {
		return repository.findAllToSelectVO();
	}

}
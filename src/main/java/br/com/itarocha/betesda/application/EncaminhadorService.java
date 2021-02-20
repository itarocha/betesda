package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.EncaminhadorRepository;
import br.com.itarocha.betesda.application.port.in.EncaminhadorUseCase;
import br.com.itarocha.betesda.domain.Encaminhador;
import br.com.itarocha.betesda.domain.SelectValueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EncaminhadorService implements EncaminhadorUseCase {

	private final EncaminhadorRepository repository;

	public Encaminhador create(Encaminhador model) {
		try{
			repository.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<Encaminhador> model = find(id);
		if (model.isPresent()) {
			repository.delete(model.get());
		}
	}

	public Encaminhador update(Encaminhador model) {
		Optional<Encaminhador> obj = find(model.getId());
		if (obj.isPresent()) {
			return repository.save(model);
		}
		return model;
	}

	public Optional<Encaminhador> find(Long id) {
		return repository.findById(id);
	}

	public List<Encaminhador> findAllByEntidadeId(Long entidadeId) {
		return repository.findAllByEntidadeId(entidadeId);
	}

	public List<SelectValueVO> listSelectByEntidadeId(Long entidadeId) {
		return repository.findAllByEntidadeIdToSelectVO(entidadeId);
	}
	
}

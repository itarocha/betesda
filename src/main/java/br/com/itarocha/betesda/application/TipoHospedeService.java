package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.TipoHospedeRepository;
import br.com.itarocha.betesda.application.port.in.TipoHospedeUseCase;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.TipoHospede;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoHospedeService implements TipoHospedeUseCase {

	private final TipoHospedeRepository repository;

	@Override
	public TipoHospede create(TipoHospede model) {
		try{
			return repository.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	@Override
	public TipoHospede update(TipoHospede model) {
		Optional<TipoHospede> result = repository.findById(model.getId());
		return result.isPresent() ? repository.save(result.get()) : null;
	}

	@Override
	public TipoHospede find(Long id) {
		Optional<TipoHospede> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public List<TipoHospede> findAll() {
		return repository.findAllOrderByDescricao();
	}

	@Override
	public List<SelectValueVO> listSelect() {
		return repository.findAllToSelectVO();
	}
}

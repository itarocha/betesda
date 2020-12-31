package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.SituacaoLeitoRepository;
import br.com.itarocha.betesda.application.port.in.SituacaoLeitoUseCase;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SituacaoLeitoService implements SituacaoLeitoUseCase {

	private final SituacaoLeitoRepository repository;

	@Override
	public SituacaoLeito create(SituacaoLeito model) {
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
	public SituacaoLeito find(Long id) {
		Optional<SituacaoLeito> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public List<SituacaoLeito> findAll() {
		return repository.findAllOrderByDescricao();
	}

	@Override
	public List<SelectValueVO> listSelect() {
		return repository.findAllToSelectVO();
	}

}

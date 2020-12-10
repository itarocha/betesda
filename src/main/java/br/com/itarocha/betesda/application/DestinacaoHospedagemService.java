package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.application.port.in.DestinacaoHospedagemUseCase;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemService implements DestinacaoHospedagemUseCase {

	private final DestinacaoHospedagemRepository repository;

	public DestinacaoHospedagem create(DestinacaoHospedagem model) {
		try{
			return repository.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(repository::delete);
	}
	
	public DestinacaoHospedagem find(Long id) {
		Optional<DestinacaoHospedagem> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	public List<DestinacaoHospedagem> findAll() {
		return repository.findAllOrderByDescricao();
	}

	public List<SelectValueVO> listSelect() {
		return repository.findAllToSelectVO();
	}

}

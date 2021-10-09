package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.out.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.core.ports.in.DestinacaoHospedagemUseCase;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.ItemDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemService implements DestinacaoHospedagemUseCase {

	private final DestinacaoHospedagemRepository repository;

	@Override
	public DestinacaoHospedagem create(DestinacaoHospedagem model) {
		try{
			return repository.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public void remove(Long id) {
		repository.findById(id).ifPresent(repository::delete);
	}

	@Override
	public DestinacaoHospedagem find(Long id) {
		Optional<DestinacaoHospedagem> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@Override
	public List<DestinacaoHospedagem> findAll() {
		return repository.findAllOrderByDescricao();
	}

	@Override
	public List<ItemDictionary> listSelect() {
		return repository.findAllToSelectVO();
	}

}

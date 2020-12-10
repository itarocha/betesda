package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.application.out.DestinacaoHospedagemRepository;
import br.com.itarocha.betesda.application.port.in.DestinacaoHospedagemUseCase;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.DestinacaoHospedagemJpaRepository;
import br.com.itarocha.betesda.domain.dto.DestinacaoHospedagemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}
	
	public DestinacaoHospedagem find(Long id) {
		Optional<DestinacaoHospedagem> result = repository.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	public List<DestinacaoHospedagem> findAll() {
		return repository.findAllOrderByDescricao()
				.stream()
				.collect(Collectors.toList());
	}

	public List<SelectValueVO> listSelect() {
		return repository.findAllToSelectVO();
	}

}

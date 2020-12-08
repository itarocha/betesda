package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.adapter.out.persistence.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.repository.DestinacaoHospedagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemService {

	private final DestinacaoHospedagemMapper mapper;

	private final DestinacaoHospedagemRepository repository;

	public DestinacaoHospedagem create(DestinacaoHospedagemEntity model) {
		try{
			return mapper.toModel(repository.save(model));
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}
	
	public DestinacaoHospedagem find(Long id) {
		Optional<DestinacaoHospedagemEntity> result = repository.findById(id);
		return result.isPresent() ? mapper.toModel(result.get()) : null;
	}

	public List<DestinacaoHospedagem> findAll() {
		return repository.findAllOrderByDescricao()
				.stream()
				.map(mapper::toModel)
				.collect(Collectors.toList());
	}

	public List<SelectValueVO> listSelect() {
		return repository.findAllOrderByDescricao()
				.stream()
				.map(mapper::toModel)
				.map(mapper::toSelectValueVO)
				.collect(Collectors.toList());
	}

}

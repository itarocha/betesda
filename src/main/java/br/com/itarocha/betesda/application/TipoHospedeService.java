package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoHospedeMapper;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoHospedeEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoHospedeJpaRepository;
import br.com.itarocha.betesda.domain.TipoHospede;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoHospedeService {

	private final TipoHospedeMapper mapper;
	private final TipoHospedeJpaRepository repository;

	public TipoHospede create(TipoHospedeEntity model) {
		try{
			return mapper.toModel(repository.save(model));
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	public TipoHospede update(TipoHospedeEntity model) {
		Optional<TipoHospedeEntity> result = repository.findById(model.getId());
		return result.isPresent() ? mapper.toModel(repository.save(result.get())) : null;
	}

	public TipoHospede find(Long id) {
		Optional<TipoHospedeEntity> result = repository.findById(id);
		return result.isPresent() ? mapper.toModel(result.get()) : null;
	}

	public List<TipoHospede> findAll() {
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

package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoServicoMapper;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoServicoJpaRepository;
import br.com.itarocha.betesda.domain.TipoServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoServicoService {

	private final TipoServicoMapper mapper;
	private final TipoServicoJpaRepository repository;

	public TipoServico create(TipoServicoEntity model) {
		try{
			return mapper.toModel(repository.save(model));
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	public TipoServico update(TipoServicoEntity model) {
		Optional<TipoServicoEntity> optional = repository.findById(model.getId());
		if (optional != null) {
			return mapper.toModel(repository.save(model));
		}
		return mapper.toModel(model);
	}

  	public TipoServico find(Long id) {
		Optional<TipoServicoEntity> result = repository.findById(id);
		return result.isPresent() ? mapper.toModel(result.get()) : null;
	}

	public List<TipoServico> findAll() {
		return repository.findAllOrderByDescricao()
				.stream()
				.map(mapper::toModel)
				.collect(Collectors.toList());
	}
	
	public List<SelectValueVO> listSelect() {
		return repository.findAllAtivosOrderByDescricao()
				.stream()
				.map(mapper::toModel)
				.map(mapper::toSelectValueVO)
				.collect(Collectors.toList());
	}
}

package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.mapper.SituacaoLeitoMapper;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.SituacaoLeitoJpaRepository;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SituacaoLeitoService {

	private final SituacaoLeitoMapper mapper;

	private final SituacaoLeitoJpaRepository repository;

	public SituacaoLeito create(SituacaoLeitoEntity model) {
		try{
			return mapper.toModel(repository.save(model));
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}		
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	public SituacaoLeito find(Long id) {
		Optional<SituacaoLeitoEntity> result = repository.findById(id);
		return result.isPresent() ? mapper.toModel(result.get()) : null;
	}

	public List<SituacaoLeito> findAll() {
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

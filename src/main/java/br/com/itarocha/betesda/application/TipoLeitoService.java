package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.mapper.TipoLeitoMapper;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.TipoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.TipoLeitoJpaRepository;
import br.com.itarocha.betesda.domain.TipoLeito;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TipoLeitoService {

	private TipoLeitoJpaRepository repository;
	private final TipoLeitoMapper mapper;

	public TipoLeito create(TipoLeitoEntity model) {
		try{
			return mapper.toModel(repository.save(model));
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		repository.findById(id).ifPresent(model -> repository.delete(model));
	}

	public TipoLeito update(TipoLeitoEntity model) {
		Optional<TipoLeitoEntity> result = repository.findById(model.getId());
		return result.isPresent() ? mapper.toModel(repository.save(result.get())) : null;
	}
	  
  	public TipoLeito find(Long id) {
		Optional<TipoLeitoEntity> result = repository.findById(id);
		return result.isPresent() ? mapper.toModel(result.get()) : null;
	}

	public List<TipoLeito> findAll() {
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

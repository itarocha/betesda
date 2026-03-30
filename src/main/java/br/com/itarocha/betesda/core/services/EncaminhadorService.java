package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.EncaminhadorJPARepository;
import br.com.itarocha.betesda.core.ports.out.EncaminhadorPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EncaminhadorService implements EncaminhadorPort {

	private final EncaminhadorJPARepository repositorio;

	public EncaminhadorEntity create(EncaminhadorEntity model) {
		try{
			repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<EncaminhadorEntity> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public EncaminhadorEntity update(EncaminhadorEntity model) {
		Optional<EncaminhadorEntity> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<EncaminhadorEntity> find(Long id) {
		return repositorio.findById(id);
	}

	public List<EncaminhadorEntity> findAll(Long entidadeId) {
		return repositorio.findAllByEntidadeId(entidadeId);
	}

	public List<ValorTexto> listSelect(Long entidadeId) {
		return repositorio.findAllByEntidadeId(entidadeId)
				.stream().map(tb -> new ValorTexto(tb.getId(), tb.getNome()))
				.toList();
	}
	
}

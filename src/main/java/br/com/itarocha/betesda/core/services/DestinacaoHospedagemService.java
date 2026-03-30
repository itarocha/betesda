package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.core.domain.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.DestinacaoHospedagemJPARepository;
import br.com.itarocha.betesda.core.ports.out.DestinacaoHospedagemPort;
import br.com.itarocha.betesda.mapper.DestinacaoHospedagemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemService implements DestinacaoHospedagemPort {

	private final DestinacaoHospedagemJPARepository repositorio;
	private final DestinacaoHospedagemMapper mapper;

	public DestinacaoHospedagem create(DestinacaoHospedagem model) {
		try{
			DestinacaoHospedagemEntity saved = repositorio.save(mapper.modelToEntity(model));
			return mapper.entityToModel(saved);
		} catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		Optional<DestinacaoHospedagemEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			repositorio.delete(retorno.get());
		}
	}
	
	public DestinacaoHospedagem find(Long id) {
		Optional<DestinacaoHospedagemEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return mapper.entityToModel(retorno.get());
		} else {
			return null;
		}
	}

	public List<DestinacaoHospedagem> findAll() {
		return mapper.listEntitytoListModel(repositorio.findAll());
	}

	public List<ValorTexto> listSelect() {
		return repositorio.findAll()
				.stream().map(tb -> new ValorTexto(tb.getId(), tb.getDescricao()))
				.toList();
	}
	
}

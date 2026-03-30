package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.DestinacaoHospedagemJPARepository;
import br.com.itarocha.betesda.core.ports.out.DestinacaoHospedagemPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DestinacaoHospedagemService implements DestinacaoHospedagemPort {

	private final DestinacaoHospedagemJPARepository repositorio;

	public DestinacaoHospedagemEntity create(DestinacaoHospedagemEntity model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		DestinacaoHospedagemEntity model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}
	
	public DestinacaoHospedagemEntity find(Long id) {
		Optional<DestinacaoHospedagemEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<DestinacaoHospedagemEntity> findAll() {
		return repositorio.findAll();
	}

	public List<ValorTexto> listSelect() {
		return repositorio.findAll()
				.stream().map(tb -> new ValorTexto(tb.getId(), tb.getDescricao()))
				.toList();
	}
	
}

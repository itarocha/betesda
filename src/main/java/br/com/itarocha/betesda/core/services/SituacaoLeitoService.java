package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.SituacaoLeitoJPARepository;
import br.com.itarocha.betesda.core.ports.out.SituacaoLeitoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SituacaoLeitoService implements SituacaoLeitoPort {

	private final SituacaoLeitoJPARepository repositorio;

	public SituacaoLeitoEntity create(SituacaoLeitoEntity model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}		
	}

	public void remove(Long id) {
		SituacaoLeitoEntity model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}		
	}

	public SituacaoLeitoEntity find(Long id) {
		Optional<SituacaoLeitoEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<SituacaoLeitoEntity> findAll() {
		return repositorio.findAllOrderByDescricao();
	}
	
	public List<ValorTexto> listSelect() {
		List<ValorTexto> retorno = new ArrayList<ValorTexto>();
		List<SituacaoLeitoEntity> lst = repositorio.findAllOrderByDescricao();
		lst.forEach(x -> retorno.add(new ValorTexto(x.getId(), x.getDescricao())));
		return retorno;
	}

}

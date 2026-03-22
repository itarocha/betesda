package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.persistencia.model.SituacaoLeitoEntity;
import br.com.itarocha.betesda.persistencia.repository.SituacaoLeitoJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SituacaoLeitoService {

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
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		List<SituacaoLeitoEntity> lst = repositorio.findAllOrderByDescricao();
		lst.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

}

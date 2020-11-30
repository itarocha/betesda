package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.repository.SituacaoLeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SituacaoLeitoService {

	@Autowired
	private SituacaoLeitoRepository repositorio;

	public SituacaoLeito create(SituacaoLeito model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}		
	}

	public void remove(Long id) {
		SituacaoLeito model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}		
	}

	public SituacaoLeito find(Long id) {
		Optional<SituacaoLeito> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<SituacaoLeito> findAll() {
		return repositorio.findAllOrderByDescricao();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		List<SituacaoLeito> lst = repositorio.findAllOrderByDescricao();
		lst.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

}

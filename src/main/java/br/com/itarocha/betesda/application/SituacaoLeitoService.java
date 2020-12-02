package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.domain.model.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.entity.SituacaoLeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.repository.SituacaoLeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SituacaoLeitoService {

	@Autowired
	private SituacaoLeitoRepository repositorio;

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

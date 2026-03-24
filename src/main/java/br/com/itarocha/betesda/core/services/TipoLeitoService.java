package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.core.domain.model.SelectValueVO;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoLeitoEntity;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.TipoLeitoJPARepository;
import br.com.itarocha.betesda.core.ports.out.TipoLeitoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoLeitoService implements TipoLeitoPort {

	private final EntityManager em;
	
	private final TipoLeitoJPARepository repositorio;

	public TipoLeitoEntity create(TipoLeitoEntity model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		TipoLeitoEntity model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}

	public TipoLeitoEntity update(TipoLeitoEntity model) {
		TipoLeitoEntity obj = find(model.getId());
		if (obj != null) {
			obj = repositorio.save(model);
		}
		return obj;
	}
	  
  	public TipoLeitoEntity find(Long id) {
		Optional<TipoLeitoEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	//TODO: Mover query para repositório
	public List<TipoLeitoEntity> findAll() {
		return em.createQuery("SELECT e FROM TipoLeitoEntity e ORDER BY e.descricao", TipoLeitoEntity.class).getResultList();
	}

	//TODO: Mover query para repositório
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM TipoLeitoEntity o ORDER BY o.descricao",TipoLeitoEntity.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

}

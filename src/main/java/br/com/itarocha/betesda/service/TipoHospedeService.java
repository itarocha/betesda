package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.persistencia.model.TipoHospedeEntity;
import br.com.itarocha.betesda.persistencia.repository.TipoHospedeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoHospedeService {

	private final EntityManager em;
	
	private final TipoHospedeEntityRepository repositorio;

	public TipoHospedeEntity create(TipoHospedeEntity model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		TipoHospedeEntity model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}

	public TipoHospedeEntity update(TipoHospedeEntity model) {
		TipoHospedeEntity obj = find(model.getId());
		if (obj != null) {
			obj = repositorio.save(model);
		}
		return obj;
	}

	public TipoHospedeEntity find(Long id) {
		Optional<TipoHospedeEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<TipoHospedeEntity> findAll() {
		return em.createQuery("SELECT e FROM TipoHospedeEntity e ORDER BY e.descricao", TipoHospedeEntity.class).getResultList();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM TipoHospedeEntity o ORDER BY o.descricao",TipoHospedeEntity.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
	
}

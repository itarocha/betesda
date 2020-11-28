package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.model.TipoHospede;
import br.com.itarocha.betesda.repository.TipoHospedeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TipoHospedeService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private TipoHospedeRepository repositorio;

	public TipoHospede create(TipoHospede model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		TipoHospede model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}

	public TipoHospede update(TipoHospede model) {
		TipoHospede obj = find(model.getId());
		if (obj != null) {
			obj = repositorio.save(model);
		}
		return obj;
	}

	public TipoHospede find(Long id) {
		Optional<TipoHospede> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<TipoHospede> findAll() {
		return em.createQuery("SELECT e FROM TipoHospede e ORDER BY e.descricao", TipoHospede.class).getResultList();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM TipoHospede o ORDER BY o.descricao",TipoHospede.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
	
}

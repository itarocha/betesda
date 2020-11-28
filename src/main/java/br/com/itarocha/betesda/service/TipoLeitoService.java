package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.model.TipoLeito;
import br.com.itarocha.betesda.repository.TipoLeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TipoLeitoService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private TipoLeitoRepository repositorio;

	public TipoLeito create(TipoLeito model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		TipoLeito model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}

	public TipoLeito update(TipoLeito model) {
		TipoLeito obj = find(model.getId());
		if (obj != null) {
			obj = repositorio.save(model);
		}
		return obj;
	}
	  
  	public TipoLeito find(Long id) {
		Optional<TipoLeito> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<TipoLeito> findAll() {
		return em.createQuery("SELECT e FROM TipoLeito e ORDER BY e.descricao", TipoLeito.class).getResultList();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM TipoLeito o ORDER BY o.descricao",TipoLeito.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
	
	/*	
	@Autowired
	private EntityManager em;
	
	@Autowired
	private TipoHospedeRepository repositorio;
	
	public DestinacaoHospedagem create(DestinacaoHospedagem model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		DestinacaoHospedagem model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}
	
	public DestinacaoHospedagem find(Long id) {
		return em.find(DestinacaoHospedagem.class, id);
	}

	public List<DestinacaoHospedagem> findAll() {
		TypedQuery query = em.createQuery("SELECT model FROM DestinacaoHospedagem model ORDER BY model.descricao", DestinacaoHospedagem.class);
		return query.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT model FROM DestinacaoHospedagem model ORDER BY model.descricao", DestinacaoHospedagem.class)
		.getResultList()
		.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

*/	
	
}

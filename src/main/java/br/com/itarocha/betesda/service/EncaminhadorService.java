package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.Encaminhador;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.repository.EncaminhadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EncaminhadorService {

	@Autowired
	private EntityManager em;

	@Autowired
	private EncaminhadorRepository repositorio;

	
	public EncaminhadorService() {
	}

	public Encaminhador create(Encaminhador model) {
		try{
			repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<Encaminhador> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public Encaminhador update(Encaminhador model) {
		Optional<Encaminhador> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<Encaminhador> find(Long id) {
		return repositorio.findById(id);
	}

	public List<Encaminhador> findAll(Long entidadeId) {
		return em.createQuery("SELECT model FROM Encaminhador model WHERE model.entidade.id = :entidadeId ORDER BY model.nome", Encaminhador.class)
				.setParameter("entidadeId", entidadeId)
				.getResultList();
	}

	//TODO Por código da entidade
	public List<SelectValueVO> listSelect(Long entidadeId) {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT model FROM Encaminhador model WHERE model.entidade.id = :entidadeId AND model.ativo = 'S' ORDER BY model.nome",Encaminhador.class)
			.setParameter("entidadeId", entidadeId)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getNome())));
		return retorno;
	}
	
}

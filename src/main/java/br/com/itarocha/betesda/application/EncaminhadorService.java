package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EncaminhadorEntity;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.EncaminhadorJpaRepository;
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
	private EncaminhadorJpaRepository repositorio;

	
	public EncaminhadorService() {
	}

	public EncaminhadorEntity create(EncaminhadorEntity model) {
		try{
			repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<EncaminhadorEntity> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public EncaminhadorEntity update(EncaminhadorEntity model) {
		Optional<EncaminhadorEntity> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<EncaminhadorEntity> find(Long id) {
		return repositorio.findById(id);
	}

	public List<EncaminhadorEntity> findAll(Long entidadeId) {
		return em.createQuery("SELECT model FROM EncaminhadorEntity model WHERE model.entidade.id = :entidadeId ORDER BY model.nome", EncaminhadorEntity.class)
				.setParameter("entidadeId", entidadeId)
				.getResultList();
	}

	//TODO Por código da entidade
	public List<SelectValueVO> listSelect(Long entidadeId) {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT model FROM EncaminhadorEntity model WHERE model.entidade.id = :entidadeId AND model.ativo = 'S' ORDER BY model.nome", EncaminhadorEntity.class)
			.setParameter("entidadeId", entidadeId)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getNome())));
		return retorno;
	}
	
}

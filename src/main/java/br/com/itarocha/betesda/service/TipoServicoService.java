package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.persistencia.model.TipoServicoEntity;
import br.com.itarocha.betesda.persistencia.repository.TipoServicoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoServicoService {

	private final EntityManager em;
	
	private final TipoServicoEntityRepository repositorio;

	public TipoServicoEntity create(TipoServicoEntity model) {
		repositorio.save(model);
		return model;
	}

	public void remove(Long id) {
		TipoServicoEntity emp = find(id);
		if (emp != null) {
			repositorio.delete(emp);
		}
	}

	public TipoServicoEntity update(TipoServicoEntity model) {
		TipoServicoEntity obj = find(model.getId());
		if (obj != null) {
			obj = em.merge(model);
		}
		return obj;
	}

  	public TipoServicoEntity find(Long id) {
		Optional<TipoServicoEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<TipoServicoEntity> findAll() {
		return em.createQuery("SELECT e FROM TipoServicoEntity e ORDER BY e.descricao", TipoServicoEntity.class).getResultList();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM TipoServicoEntity o WHERE o.ativo = 'S' ORDER BY o.descricao",TipoServicoEntity.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
}

package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoServicoEntity;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.TipoServicoJPARepository;
import br.com.itarocha.betesda.core.ports.out.TipoServicoPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoServicoService implements TipoServicoPort {

	private final EntityManager em;
	
	private final TipoServicoJPARepository repositorio;

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

	//TODO: Mover query para repositório
	public List<TipoServicoEntity> findAll() {
		return em.createQuery("SELECT e FROM TipoServicoEntity e ORDER BY e.descricao", TipoServicoEntity.class).getResultList();
	}

	//TODO: Mover query para repositório
	public List<ValorTexto> listSelect() {
		List<ValorTexto> retorno = new ArrayList<ValorTexto>();
		em.createQuery("SELECT o FROM TipoServicoEntity o WHERE o.ativo = 'S' ORDER BY o.descricao",TipoServicoEntity.class)
			.getResultList()
			.forEach(x -> retorno.add(new ValorTexto(x.getId(), x.getDescricao())));
		return retorno;
	}
}

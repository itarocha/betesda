package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.domain.model.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.repository.DestinacaoHospedagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DestinacaoHospedagemService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private DestinacaoHospedagemRepository repositorio;

	public DestinacaoHospedagemEntity create(DestinacaoHospedagemEntity model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		DestinacaoHospedagemEntity model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}
	
	public DestinacaoHospedagemEntity find(Long id) {
		Optional<DestinacaoHospedagemEntity> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

	public List<DestinacaoHospedagemEntity> findAll() {
		TypedQuery query = em.createQuery("SELECT model FROM DestinacaoHospedagem model ORDER BY model.descricao", DestinacaoHospedagemEntity.class);
		return query.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT model FROM DestinacaoHospedagem model ORDER BY model.descricao", DestinacaoHospedagemEntity.class)
		.getResultList()
		.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
	
}

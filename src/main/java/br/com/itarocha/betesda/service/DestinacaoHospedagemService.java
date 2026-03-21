package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.repository.DestinacaoHospedagemEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DestinacaoHospedagemService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private DestinacaoHospedagemEntityRepository repositorio;

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
		TypedQuery query = em.createQuery("SELECT model FROM DestinacaoHospedagemEntity model ORDER BY model.descricao", DestinacaoHospedagemEntity.class);
		return query.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT model FROM DestinacaoHospedagemEntity model ORDER BY model.descricao", DestinacaoHospedagemEntity.class)
		.getResultList()
		.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
	
}

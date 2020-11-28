package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.repository.DestinacaoHospedagemRepository;
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
		Optional<DestinacaoHospedagem> retorno = repositorio.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
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
	
}

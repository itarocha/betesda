package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.repository.EnderecoRepository;
import br.com.itarocha.betesda.repository.EntidadeRepository;
import br.com.itarocha.betesda.util.validation.ResultError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EntidadeService {

	@Autowired
	private EntityManager em;

	@Autowired
	private EntidadeRepository repositorio;

	@Autowired
	private EnderecoRepository enderecoRepo;

	public EntidadeService() {
	}

	public Entidade create(Entidade model) throws ValidationException {
		try{
			
			Long id = model.getId() == null ? 0L : model.getId();
			
			if (this.entidadeCadastradaPorCampo(id, "cnpj", model.getCnpj())) {
				throw new ValidationException(new ResultError().addError("cnpj", "CNPJ já casdastrado para outra Entidade"));
			}
			
			enderecoRepo.save(model.getEndereco());
			repositorio.save(model);
		} catch (ValidationException e) {
			throw e;
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<Entidade> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public Entidade update(Entidade model) {
		Optional<Entidade> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<Entidade> find(Long id) {
		return repositorio.findById(id);
	}

	public List<Entidade> findAll() {
		return em.createQuery("SELECT model FROM Entidade model ORDER BY model.nome", Entidade.class).getResultList();
	}

	public List<Entidade> consultar(String texto) {
		return em.createQuery("SELECT model FROM Entidade model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", Entidade.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT e FROM Entidade e ORDER BY e.nome",Entidade.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getNome())));
		return retorno;
	}
	
	public boolean entidadeCadastradaPorCampo(Long entidadeId, String campo, String valor) {
		
		if ("".equals(valor) || valor == null) {
			return false;
		}
		Long qtd = 0L;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT COUNT(*) "); 
			sb.append("FROM entidade "); 
			sb.append(String.format("WHERE %s = :%s ",campo,campo)); 
			sb.append("AND id <> :entidadeId ");
			
			Query query = em.createNativeQuery(sb.toString());
			qtd = ((Number)query.setParameter("entidadeId", entidadeId)
									.setParameter(campo, valor)
									.getSingleResult()).longValue();
		} catch (Exception e) {
			return false;
		}
		
		return qtd > 0; 
	}
}

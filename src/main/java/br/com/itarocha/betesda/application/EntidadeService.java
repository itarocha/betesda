package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.adapter.out.persistence.entity.EntidadeEntity;
import br.com.itarocha.betesda.domain.model.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.repository.EnderecoRepository;
import br.com.itarocha.betesda.adapter.out.persistence.repository.EntidadeRepository;
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

	public EntidadeEntity create(EntidadeEntity model) throws ValidationException {
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
		Optional<EntidadeEntity> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public EntidadeEntity update(EntidadeEntity model) {
		Optional<EntidadeEntity> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<EntidadeEntity> find(Long id) {
		return repositorio.findById(id);
	}

	public List<EntidadeEntity> findAll() {
		return em.createQuery("SELECT model FROM Entidade model ORDER BY model.nome", EntidadeEntity.class).getResultList();
	}

	public List<EntidadeEntity> consultar(String texto) {
		return em.createQuery("SELECT model FROM Entidade model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", EntidadeEntity.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT e FROM Entidade e ORDER BY e.nome", EntidadeEntity.class)
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

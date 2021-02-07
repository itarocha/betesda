package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.exception.ObsoleteValidationException;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.EnderecoJpaRepository;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.EntidadeJpaRepository;
import br.com.itarocha.betesda.util.validation.EntityValidationError;
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
	private EntidadeJpaRepository repositorio;

	@Autowired
	private EnderecoJpaRepository enderecoRepo;

	public EntidadeService() {
	}

	public EntidadeEntity create(EntidadeEntity model) throws ObsoleteValidationException {
		try{
			
			Long id = model.getId() == null ? 0L : model.getId();
			
			if (this.entidadeCadastradaPorCampo(id, "cnpj", model.getCnpj())) {
				throw new ObsoleteValidationException(EntityValidationError.builder().build().addError("cnpj", "CNPJ já casdastrado para outra Entidade"));
			}
			
			enderecoRepo.save(model.getEndereco());
			repositorio.save(model);
		} catch (ObsoleteValidationException e) {
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
		return em.createQuery("SELECT model FROM EntidadeEntity model ORDER BY model.nome", EntidadeEntity.class).getResultList();
	}

	public List<EntidadeEntity> consultar(String texto) {
		return em.createQuery("SELECT model FROM EntidadeEntity model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", EntidadeEntity.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT e FROM EntidadeEntity e ORDER BY e.nome", EntidadeEntity.class)
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

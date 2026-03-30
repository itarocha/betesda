package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.core.ports.out.EntidadePort;
import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.EnderecoJPARepository;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.EntidadeJPARepository;
import br.com.itarocha.betesda.core.validation.ResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntidadeService implements EntidadePort {

	private final EntityManager em;

	private final EntidadeJPARepository repositorio;

	private final EnderecoJPARepository enderecoRepo;

	public EntidadeEntity create(EntidadeEntity model) {

		try {
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
		return repositorio.findAll();
	}

	//TODO: Mover query para repositório
	public List<EntidadeEntity> consultar(String texto) {
		return em.createQuery("SELECT model FROM EntidadeEntity model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", EntidadeEntity.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}

	public List<ValorTexto> listSelect() {
		return repositorio.findAll()
				.stream().map(tb -> new ValorTexto(tb.getId(), tb.getNome()))
				.toList();
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

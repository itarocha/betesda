package br.com.itarocha.betesda.core.services;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EnderecoEntity;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.PessoaEntity;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.EnderecoJPARepository;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.repository.PessoaJPARepository;
import br.com.itarocha.betesda.core.validation.ResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.Path;
import java.util.List;
import java.util.Optional;

//import static org.springframework.data.jpa.domain.Specifications.where;

@Service
@RequiredArgsConstructor
public class PessoaService {

	private final EntityManager em;

	private final PessoaJPARepository repositorio;

	private final EnderecoJPARepository enderecoRepo;

	public PessoaEntity create(PessoaEntity model) throws ValidationException {
		try{
			enderecoRepo.save(model.getEndereco());
			
			Long id = model.getId() == null ? 0L : model.getId();
			
			if (this.pessoaCadastradaPorCampo(id, "cpf", model.getCpf())) {
				throw new ValidationException(new ResultError().addError("cpf", "CPF já casdastrado para outra pessoa"));
			}
			
			if (this.pessoaCadastradaPorCampo(id, "rg", model.getRg())) {
				throw new ValidationException(new ResultError().addError("rg", "RG já casdastrado para outra pessoa"));
			}
			
			if (this.pessoaCadastradaPorCampo(id, "cartao_sus", model.getCartaoSus())) {
				throw new ValidationException(new ResultError().addError("cartaoSus", "Cartão do SUS já casdastrado para outra pessoa"));
			}
			
			repositorio.save(model);
		} catch (ValidationException e) {
			throw e;
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<PessoaEntity> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public PessoaEntity update(PessoaEntity model) {
		Optional<PessoaEntity> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<PessoaEntity> find(Long id) {
		return repositorio.findById(id);
	}

	public List<PessoaEntity> findByFieldNameAndValue(String campo, String valor){
		return repositorio.findAll(campoQueContenha(campo, valor));
	}

	//TODO: Mover query para repositório
	public List<PessoaEntity> findAll() {
		return em.createQuery("SELECT model FROM PessoaEntity model ORDER BY model.nome", PessoaEntity.class).getResultList();
	}

	//TODO: Mover query para repositório
	public List<PessoaEntity> consultar(String texto) {
		return em.createQuery("SELECT model FROM PessoaEntity model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", PessoaEntity.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}
	
	static Specification<PessoaEntity> campoIgual(String campo, String valor) {
	    return (pessoa, cq, cb) -> cb.equal(pessoa.get(campo), valor);
	}
	 
	static Specification<PessoaEntity> endereco(String campo, String valor) {
	    return (pessoa, cq, cb) -> {
	    	Path<EnderecoEntity> endereco = pessoa.<EnderecoEntity> get("endereco");
	    	
	    	return cb.equal(endereco.get(campo).as(String.class),valor);
	    };
	}

	static Specification<PessoaEntity> campoContem(String campo, String valor) {
	    return (pessoa, cq, cb) -> cb.like(cb.lower(pessoa.get(campo)), "%" + valor.toLowerCase() + "%");
	}
	
	public static Specification<PessoaEntity> campoQueContenha(String campo, String conteudo) {
		// https://leaks.wanari.com/2018/01/23/awesome-spring-specification/
		return (root, query, cb) -> {
			query.distinct(true);
			query.orderBy(cb.asc(root.get("nome")));
			//Path<EnderecoEntity> endereco = root.<EnderecoEntity> get("endereco");
            
            //Predicate endfilter = cb.equal(endereco.get("uf").as(String.class),"MG");
			//return Specification.where(campoContem(campo, conteudo)).or(endereco("uf","MA")).toPredicate(root, query, cb);
			
			return Specification.where(campoContem(campo, conteudo)).toPredicate(root, query, cb);
		};
		
		
		/*
        return new Specification<PessoaEntity>() {
			private static final long serialVersionUID = 575273514861865441L;

			@Override
            public Predicate toPredicate(Root<PessoaEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //Join<PessoaEntity, EnderecoEntity> endereco = root.join("endereco");
                //
                //criteriaQuery.equal(endereco.get("uf"),"MG");
                
                Path<EnderecoEntity> endereco = root.<EnderecoEntity> get("endereco");
                
                Predicate endfilter = cb.equal(endereco.get("uf").as(String.class),"MG");
                
                //cb.and(endfilter);
                cb.like(root.get(campo), conteudo);
                query.orderBy(cb.asc(root.get("nome")));
                
                //return cb.and(endfilter);
            }
        };
        */
    }

	//TODO: Mover query para repositório
	public boolean pessoaCadastradaPorCampo(Long pessoaId, String campo, String valor) {
		
		if ("".equals(valor) || valor == null) {
			return false;
		}
		Long qtd = 0L;
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT COUNT(*) "); 
			sb.append("FROM pessoa "); 
			sb.append(String.format("WHERE %s = :%s ",campo,campo)); 
			sb.append("AND id <> :pessoaId ");
			
			Query query = em.createNativeQuery(sb.toString());
			qtd = ((Number)query.setParameter("pessoaId", pessoaId)
									.setParameter(campo, valor)
									.getSingleResult()).longValue();
		} catch (Exception e) {
			return false;
		}
		
		return qtd > 0; 
	}
	
}

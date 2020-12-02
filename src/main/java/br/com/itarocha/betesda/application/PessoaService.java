package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.adapter.out.persistence.entity.EnderecoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.entity.PessoaEntity;
import br.com.itarocha.betesda.adapter.out.persistence.repository.EnderecoRepository;
import br.com.itarocha.betesda.adapter.out.persistence.repository.PessoaRepository;
import br.com.itarocha.betesda.util.validation.ResultError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.Path;
import java.util.List;
import java.util.Optional;

//import static org.springframework.data.jpa.domain.Specifications.where;

@Service
public class PessoaService {

	@Autowired
	private EntityManager em;

	@Autowired
	private PessoaRepository repositorio;

	@Autowired
	private EnderecoRepository enderecoRepo;

	public PessoaService() {
	}

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
	
	public List<PessoaEntity> findAll() {
		return em.createQuery("SELECT model FROM Pessoa model ORDER BY model.nome", PessoaEntity.class).getResultList();
	}

	public List<PessoaEntity> consultar(String texto) {
		return em.createQuery("SELECT model FROM Pessoa model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", PessoaEntity.class)
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
			//Path<Endereco> endereco = root.<Endereco> get("endereco");
            
            //Predicate endfilter = cb.equal(endereco.get("uf").as(String.class),"MG");
			//return Specification.where(campoContem(campo, conteudo)).or(endereco("uf","MA")).toPredicate(root, query, cb);
			
			return Specification.where(campoContem(campo, conteudo)).toPredicate(root, query, cb);
		};
		
		
		/*
        return new Specification<Pessoa>() {
			private static final long serialVersionUID = 575273514861865441L;

			@Override
            public Predicate toPredicate(Root<Pessoa> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //Join<Pessoa, Endereco> endereco = root.join("endereco");
                //
                //criteriaQuery.equal(endereco.get("uf"),"MG");
                
                Path<Endereco> endereco = root.<Endereco> get("endereco");
                
                Predicate endfilter = cb.equal(endereco.get("uf").as(String.class),"MG");
                
                //cb.and(endfilter);
                cb.like(root.get(campo), conteudo);
                query.orderBy(cb.asc(root.get("nome")));
                
                //return cb.and(endfilter);
            }
        };
        */
    }
	
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

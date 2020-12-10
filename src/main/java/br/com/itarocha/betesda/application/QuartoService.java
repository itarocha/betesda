package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.adapter.out.persistence.jpa.entity.*;
import br.com.itarocha.betesda.adapter.out.persistence.jpa.repository.*;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import br.com.itarocha.betesda.domain.EditLeitoVO;
import br.com.itarocha.betesda.domain.EditQuartoVO;
import br.com.itarocha.betesda.domain.NovoQuartoVO;
import br.com.itarocha.betesda.domain.SelectValueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
//https://www.devmedia.com.br/conheca-o-spring-transactional-annotations/32472
//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
public class QuartoService {

	@Autowired
	private QuartoJpaRepository quartoRepo;
	
	@Autowired
	private LeitoJpaRepository leitoRepo;
	
	@Autowired
	private TipoLeitoJpaRepository tipoLeitoRepo;
	
	@Autowired
	private SituacaoLeitoJpaRepository situacaoLeitoRepo;
	
	@Autowired
	private DestinacaoHospedagemJpaRepository destinacaoHospedagemRepo;

	public QuartoEntity create(QuartoEntity model) {
		try{
			return quartoRepo.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public QuartoEntity create(NovoQuartoVO model) throws Exception{
		QuartoEntity q = new QuartoEntity();
		try {
			TipoLeitoEntity tipoLeitoEntity = tipoLeitoRepo.getOne(model.getTipoLeito());
			SituacaoLeitoEntity situacao = situacaoLeitoRepo.getOne(model.getSituacao());
			
			q.setNumero(model.getNumero());
			q.setDescricao(model.getDescricao());

			for (Long id : model.getDestinacoes()) {
				DestinacaoHospedagemEntity dh = destinacaoHospedagemRepo.getOne(id);
				if (dh != null) {
					q.getDestinacoes().add(dh);
				}
			}

			//DestinacaoHospedagem dest = destinacaoHospedagemRepo.getOne(model.getDestinacaoHospedagem());
			//q.setDestinacaoHospedagem(dest);
			q.setAtivo(LogicoEnum.S);
			
			quartoRepo.save(q);
			
			for (int i = 1; i <= model.getQuantidadeLeitos(); i++) {
				LeitoEntity leitoEntity = new LeitoEntity();
				leitoEntity.setQuarto(q);
				leitoEntity.setNumero(i);
				leitoEntity.setTipoLeito(tipoLeitoEntity);
				leitoEntity.setSituacao(situacao);
				
				leitoRepo.save(leitoEntity);
			}
		} catch (Exception e) {
			throw e;
		}
		return q;
	}
	
  	public QuartoEntity find(Long id) {
		Optional<QuartoEntity> retorno = quartoRepo.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

  	public LeitoEntity findLeito(Long id) {
		Optional<LeitoEntity> retorno = leitoRepo.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

  	public LeitoEntity saveLeito(EditLeitoVO model) throws Exception{
		LeitoEntity leitoEntity;
		boolean isNovo = model.getId() == null; 
		if (isNovo) {
			leitoEntity = new LeitoEntity();
		} else {
			Optional<LeitoEntity> optLeito = leitoRepo.findById(model.getId());
			
			if (optLeito.isPresent()) {
				leitoEntity = optLeito.get();
			} else {
				throw new Exception("Leito inexistente: "+model.getId());
			}
		}
		
		try {
			TipoLeitoEntity tipoLeitoEntity = tipoLeitoRepo.getOne(model.getTipoLeito());
			SituacaoLeitoEntity situacao = situacaoLeitoRepo.getOne(model.getSituacao());
			leitoEntity.setNumero(model.getNumero());
			leitoEntity.setTipoLeito(tipoLeitoEntity);
			leitoEntity.setSituacao(situacao);
			
			if (isNovo) {
				QuartoEntity quartoEntity = quartoRepo.getOne(model.getQuartoId());
				leitoEntity.setQuarto(quartoEntity);
			}
			leitoEntity = leitoRepo.save(leitoEntity);
		} catch (Exception e) {
			throw e;
		}
		return leitoEntity;
	}
	
	public void remove(Long id) {
		Optional<QuartoEntity> model = quartoRepo.findById(id);
		
		if (model.isPresent()) {
			leitoRepo.deleteWhereQuartoId(model.get().getId());
			quartoRepo.delete(model.get());
		}
	}

	public void removeLeito(Long id) {
		leitoRepo.deleteById(id);
	}

	public QuartoEntity update(EditQuartoVO model) {
		Optional<QuartoEntity> oq = quartoRepo.findById(model.getId());
		QuartoEntity obj = null;
		if (oq.isPresent()) {
			obj = oq.get();
			obj.setDescricao(model.getDescricao());
			
			obj.getDestinacoes().clear();
			for (Long id : model.getDestinacoes()) {
				DestinacaoHospedagemEntity dh = destinacaoHospedagemRepo.getOne(id);
				if (dh != null) {
					obj.getDestinacoes().add(dh);
				}
			}

			// FIXME: propriedade removida
			//DestinacaoHospedagem dest = destinacaoHospedagemRepo.getOne(model.getDestinacaoHospedagem());
			//obj.setDestinacaoHospedagem(dest);
			obj.setNumero(model.getNumero());
			quartoRepo.save(obj);
		}
		return obj;
	}

	public List<QuartoEntity> findAll() {
		return quartoRepo.findAllOrderByQuartoNumero();
	}

	public List<QuartoEntity> findAllByDestinacaoHospedagem(Long id) {
		return quartoRepo.findByDestinacaoHospedagemId(id);
	}

	public List<LeitoEntity> findLeitosByQuarto(Long quartoId) {
		Optional<QuartoEntity> q = quartoRepo.findById(quartoId);
		if (!q.isPresent()) return new ArrayList<LeitoEntity>();
		
		List<LeitoEntity> lst = leitoRepo.findByQuartoId(q.get().getId());
		return lst;
	}

	public List<LeitoEntity> findLeitosDisponiveis() {
		return leitoRepo.findAllWhereDisponivel(LogicoEnum.S);
	}

	public List<SelectValueVO> listTipoLeito() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		
		List<TipoLeitoEntity> lst = tipoLeitoRepo.findAllOrderByDescricao();
		
		lst.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		
		return retorno;
	}

	public boolean existeOutroLeitoComEsseNumero(Long leito_id, Long quartoId, Integer numero) {
		Collection<LeitoEntity> lst = quartoRepo.existeOutroLeitoComEsseNumero(leito_id, quartoId, numero);
		return lst.size() > 0;
	}

	public boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero) {
		Collection<LeitoEntity> lst = quartoRepo.existeOutroLeitoComEsseNumero(quartoId, numero);
		return lst.size() > 0;
	}

	public boolean existeOutroQuartoComEsseNumero(Long id, Integer numero) {
		List<QuartoEntity> lst = quartoRepo.existeOutroQuartoComEsseNumero(id, numero);
		return lst.size() > 0;
	}
	
	public boolean existeOutroQuartoComEsseNumero(Integer numero) {
		List<QuartoEntity> lst = quartoRepo.existeOutroQuartoComEsseNumero(numero);
		return lst.size() > 0;
	}
}
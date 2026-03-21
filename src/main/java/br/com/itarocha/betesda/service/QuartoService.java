package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.model.*;
import br.com.itarocha.betesda.persistencia.model.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.persistencia.model.LeitoEntity;
import br.com.itarocha.betesda.persistencia.model.QuartoEntity;
import br.com.itarocha.betesda.persistencia.model.SituacaoLeitoEntity;
import br.com.itarocha.betesda.persistencia.model.TipoLeitoEntity;
import br.com.itarocha.betesda.persistencia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
//https://www.devmedia.com.br/conheca-o-spring-transactional-annotations/32472
//https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
public class QuartoService {

	private final QuartoEntityRepository quartoRepo; 
	
	private final LeitoEntityRepository leitoRepo;
	
	private final TipoLeitoEntityRepository tipoLeitoRepo;
	
	private final SituacaoLeitoEntityRepository situacaoLeitoRepo;
	
	private final DestinacaoHospedagemEntityRepository destinacaoHospedagemRepo;

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

			//DestinacaoHospedagemEntity dest = destinacaoHospedagemRepo.getOne(model.getDestinacaoHospedagem());
			//q.setDestinacaoHospedagem(dest);
			q.setAtivo(Logico.S);
			
			quartoRepo.save(q);
			
			for (int i = 1; i <= model.getQuantidadeLeitos(); i++) {
				LeitoEntity leito = new LeitoEntity();
				leito.setQuarto(q);
				leito.setNumero(i);
				leito.setTipoLeito(tipoLeitoEntity);
				leito.setSituacao(situacao);
				
				leitoRepo.save(leito);
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
		LeitoEntity leito;
		boolean isNovo = model.getId() == null; 
		if (isNovo) {
			leito = new LeitoEntity();
		} else {
			Optional<LeitoEntity> optLeitoEntity = leitoRepo.findById(model.getId());
			
			if (optLeitoEntity.isPresent()) {
				leito = optLeitoEntity.get();
			} else {
				throw new Exception("Leito inexistente: "+model.getId());
			}
		}
		
		try {
			TipoLeitoEntity tipoLeitoEntity = tipoLeitoRepo.getOne(model.getTipoLeito());
			SituacaoLeitoEntity situacao = situacaoLeitoRepo.getOne(model.getSituacao());
			leito.setNumero(model.getNumero());
			leito.setTipoLeito(tipoLeitoEntity);
			leito.setSituacao(situacao);
			
			if (isNovo) {
				QuartoEntity quarto = quartoRepo.getOne(model.getQuartoId());
				leito.setQuarto(quarto);
			}
			leito = leitoRepo.save(leito);
		} catch (Exception e) {
			throw e;
		}
		return leito;
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
			//DestinacaoHospedagemEntity dest = destinacaoHospedagemRepo.getOne(model.getDestinacaoHospedagem());
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
		return leitoRepo.findAllWhereDisponivel(Logico.S);
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
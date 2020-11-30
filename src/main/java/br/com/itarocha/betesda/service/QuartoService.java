package br.com.itarocha.betesda.service;

import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.domain.Quarto;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.domain.TipoLeito;
import br.com.itarocha.betesda.model.*;
import br.com.itarocha.betesda.repository.*;
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
	private QuartoRepository quartoRepo; 
	
	@Autowired
	private LeitoRepository leitoRepo;
	
	@Autowired
	private TipoLeitoRepository tipoLeitoRepo;
	
	@Autowired
	private SituacaoLeitoRepository situacaoLeitoRepo;
	
	@Autowired
	private DestinacaoHospedagemRepository destinacaoHospedagemRepo;

	public Quarto create(Quarto model) {
		try{
			return quartoRepo.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Quarto create(NovoQuartoVO model) throws Exception{
		Quarto q = new Quarto();
		try {
			TipoLeito tipoLeito = tipoLeitoRepo.getOne(model.getTipoLeito());
			SituacaoLeito situacao = situacaoLeitoRepo.getOne(model.getSituacao());
			
			q.setNumero(model.getNumero());
			q.setDescricao(model.getDescricao());

			for (Long id : model.getDestinacoes()) {
				DestinacaoHospedagem dh = destinacaoHospedagemRepo.getOne(id);
				if (dh != null) {
					q.getDestinacoes().add(dh);
				}
			}

			//DestinacaoHospedagem dest = destinacaoHospedagemRepo.getOne(model.getDestinacaoHospedagem());
			//q.setDestinacaoHospedagem(dest);
			q.setAtivo(Logico.S);
			
			quartoRepo.save(q);
			
			for (int i = 1; i <= model.getQuantidadeLeitos(); i++) {
				Leito leito = new Leito();
				leito.setQuarto(q);
				leito.setNumero(i);
				leito.setTipoLeito(tipoLeito);
				leito.setSituacao(situacao);
				
				leitoRepo.save(leito);
			}
		} catch (Exception e) {
			throw e;
		}
		return q;
	}
	
  	public Quarto find(Long id) {
		Optional<Quarto> retorno = quartoRepo.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

  	public Leito findLeito(Long id) {
		Optional<Leito> retorno = leitoRepo.findById(id);
		if (retorno.isPresent()) {
			return retorno.get(); 
		} else {
			return null;
		}
	}

  	public Leito saveLeito(EditLeitoVO model) throws Exception{
		Leito leito;
		boolean isNovo = model.getId() == null; 
		if (isNovo) {
			leito = new Leito();
		} else {
			Optional<Leito> optLeito = leitoRepo.findById(model.getId());
			
			if (optLeito.isPresent()) {
				leito = optLeito.get();
			} else {
				throw new Exception("Leito inexistente: "+model.getId());
			}
		}
		
		try {
			TipoLeito tipoLeito = tipoLeitoRepo.getOne(model.getTipoLeito());
			SituacaoLeito situacao = situacaoLeitoRepo.getOne(model.getSituacao());
			leito.setNumero(model.getNumero());
			leito.setTipoLeito(tipoLeito);
			leito.setSituacao(situacao);
			
			if (isNovo) {
				Quarto quarto = quartoRepo.getOne(model.getQuartoId());
				leito.setQuarto(quarto);
			}
			leito = leitoRepo.save(leito);
		} catch (Exception e) {
			throw e;
		}
		return leito;
	}
	
	public void remove(Long id) {
		Optional<Quarto> model = quartoRepo.findById(id);
		
		if (model.isPresent()) {
			leitoRepo.deleteWhereQuartoId(model.get().getId());
			quartoRepo.delete(model.get());
		}
	}

	public void removeLeito(Long id) {
		leitoRepo.deleteById(id);
	}

	public Quarto update(EditQuartoVO model) {
		Optional<Quarto> oq = quartoRepo.findById(model.getId());
		Quarto obj = null;
		if (oq.isPresent()) {
			obj = oq.get();
			obj.setDescricao(model.getDescricao());
			
			obj.getDestinacoes().clear();
			for (Long id : model.getDestinacoes()) {
				DestinacaoHospedagem dh = destinacaoHospedagemRepo.getOne(id);
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

	public List<Quarto> findAll() {
		return quartoRepo.findAllOrderByQuartoNumero();
	}

	public List<Quarto> findAllByDestinacaoHospedagem(Long id) {
		return quartoRepo.findByDestinacaoHospedagemId(id);
	}

	public List<Leito> findLeitosByQuarto(Long quartoId) {
		Optional<Quarto> q = quartoRepo.findById(quartoId);
		if (!q.isPresent()) return new ArrayList<Leito>();
		
		List<Leito> lst = leitoRepo.findByQuartoId(q.get().getId());
		return lst;
	}

	public List<Leito> findLeitosDisponiveis() {
		return leitoRepo.findAllWhereDisponivel(Logico.S);
	}

	public List<SelectValueVO> listTipoLeito() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		
		List<TipoLeito> lst = tipoLeitoRepo.findAllOrderByDescricao();
		
		lst.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		
		return retorno;
	}

	public boolean existeOutroLeitoComEsseNumero(Long leito_id, Long quartoId, Integer numero) {
		Collection<Leito> lst = quartoRepo.existeOutroLeitoComEsseNumero(leito_id, quartoId, numero);
		return lst.size() > 0;
	}

	public boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero) {
		Collection<Leito> lst = quartoRepo.existeOutroLeitoComEsseNumero(quartoId, numero);
		return lst.size() > 0;
	}

	public boolean existeOutroQuartoComEsseNumero(Long id, Integer numero) {
		List<Quarto> lst = quartoRepo.existeOutroQuartoComEsseNumero(id, numero);
		return lst.size() > 0;
	}
	
	public boolean existeOutroQuartoComEsseNumero(Integer numero) {
		List<Quarto> lst = quartoRepo.existeOutroQuartoComEsseNumero(numero);
		return lst.size() > 0;
	}
}
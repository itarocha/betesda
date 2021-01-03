package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.*;
import br.com.itarocha.betesda.application.port.in.QuartoUseCase;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
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
public class QuartoService implements QuartoUseCase {

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
		try {
			return quartoRepo.save(model);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Quarto create(QuartoNew model) throws Exception {
		Quarto q = new Quarto();
		try {
			TipoLeito tipoLeitoEntity = tipoLeitoRepo.findById(model.getTipoLeito()).get();
			SituacaoLeito situacao = situacaoLeitoRepo.findById(model.getSituacao()).get();

			q.setNumero(model.getNumero());
			q.setDescricao(model.getDescricao());

			for (Long id : model.getDestinacoes()) {
				DestinacaoHospedagem dh = destinacaoHospedagemRepo.findById(id).get();
				if (dh != null) {
					q.getDestinacoes().add(dh);
				}
			}

			q.setAtivo(LogicoEnum.S);
			quartoRepo.save(q);

			for (int i = 1; i <= model.getQuantidadeLeitos(); i++) {
				Leito leitoEntity = new Leito();
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

	public Leito saveLeito(EditLeitoVO model) throws Exception {
		Leito leitoEntity;
		boolean isNovo = model.getId() == null;
		if (isNovo) {
			leitoEntity = new Leito();
		} else {
			Optional<Leito> optLeito = leitoRepo.findById(model.getId());

			if (optLeito.isPresent()) {
				leitoEntity = optLeito.get();
			} else {
				throw new Exception("Leito inexistente: " + model.getId());
			}
		}

		try {
			TipoLeito tipoLeitoEntity = tipoLeitoRepo.findById(model.getTipoLeito()).get();
			SituacaoLeito situacao = situacaoLeitoRepo.findById(model.getSituacao()).get();
			leitoEntity.setNumero(model.getNumero());
			leitoEntity.setTipoLeito(tipoLeitoEntity);
			leitoEntity.setSituacao(situacao);

			if (isNovo) {
				Quarto quartoEntity = quartoRepo.findById(model.getQuartoId()).get();
				leitoEntity.setQuarto(quartoEntity);
			}
			leitoEntity = leitoRepo.save(leitoEntity);
		} catch (Exception e) {
			throw e;
		}
		return leitoEntity;
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

	public Quarto update(QuartoEdit model) {
		Optional<Quarto> oq = quartoRepo.findById(model.getId());
		Quarto obj = null;
		if (oq.isPresent()) {
			obj = oq.get();
			obj.setDescricao(model.getDescricao());

			obj.getDestinacoes().clear();
			for (Long id : model.getDestinacoes()) {
				DestinacaoHospedagem dh = destinacaoHospedagemRepo.findById(id).get();
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
		return leitoRepo.findAllWhereDisponivel(LogicoEnum.S);
	}

	public List<SelectValueVO> listTipoLeito() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();

		List<TipoLeito> lst = tipoLeitoRepo.findAllOrderByDescricao();

		lst.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));

		return retorno;
	}

	public boolean existeOutroLeitoComEsseNumero(Long leitoId, Long quartoId, Integer numero) {
		Collection<Leito> lst = quartoRepo.existeOutroLeitoComEsseNumero(leitoId, quartoId, numero);
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
package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.*;
import br.com.itarocha.betesda.application.port.in.QuartoUseCase;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class QuartoService implements QuartoUseCase {

	private final QuartoRepository quartoRepo;
	private final LeitoRepository leitoRepo;
	private final TipoLeitoRepository tipoLeitoRepo;
	private final SituacaoLeitoRepository situacaoLeitoRepo;
	private final DestinacaoHospedagemRepository destinacaoHospedagemRepo;

	public Quarto find(Long id) {
		return findQuartoById(id);
	}

	public Leito findLeito(Long id) {
		Optional<Leito> opt = leitoRepo.findById(id);
		return opt.orElse(null);
	}

	public Quarto create(Quarto model) {
		try {
			return quartoRepo.save(model);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Quarto create(QuartoNew model) throws Exception {

		Quarto q = Quarto.builder()
				.numero(model.getNumero())
				.descricao(model.getDescricao())
				.ativo(LogicoEnum.S)
				.build();

		for (Long id : model.getDestinacoes()) {
			destinacaoHospedagemRepo.findById(id).ifPresent(d -> q.getDestinacoes().add(d));
		}
		Quarto saved = quartoRepo.save(q);

		final TipoLeito tipoLeitoEntity = findTipoLeitoById(model.getTipoLeito());
		final SituacaoLeito situacao = findSituacaoLeitoById(model.getSituacao());
		IntStream.rangeClosed(1, model.getQuantidadeLeitos()-1).boxed()
				.forEach(i -> {	Leito leitoEntity = Leito.builder()
										.quarto(saved)
										.numero(i)
										.tipoLeito(tipoLeitoEntity)
										.situacao(situacao)
										.build();
								leitoRepo.save(leitoEntity);
				});
		return saved;
	}

	public Leito saveLeito(EditLeitoVO model) {
		boolean isNovo = model.getId() == null;
		Leito toSave = isNovo ? new Leito() : findLeito(model.getId());
		if (!isNovo && toSave == null){
			throw new RuntimeException("Leito inexistente: " + model.getId());
		}
		final TipoLeito tipoLeitoEntity = findTipoLeitoById(model.getTipoLeito());
		final SituacaoLeito situacao = findSituacaoLeitoById(model.getSituacao());
		toSave.setNumero(model.getNumero());
		toSave.setTipoLeito(tipoLeitoEntity);
		toSave.setSituacao(situacao);
		if (isNovo) {
			Quarto quarto = findQuartoById(model.getQuartoId());
			toSave.setQuarto(quarto);
		}
		return leitoRepo.save(toSave);
	}

	public void remove(Long id) {
		quartoRepo.findById(id).ifPresent( quarto -> {
			leitoRepo.deleteWhereQuartoId(quarto.getId());
			quartoRepo.delete(quarto);
		});
	}

	public void removeLeito(Long id) {
		leitoRepo.deleteById(id);
	}

	public Quarto update(QuartoEdit model) {
		AtomicReference<Quarto> atomicQuarto = null;
		quartoRepo.findById(model.getId()).ifPresent(quarto -> {
			quarto.getDestinacoes().clear();
			quarto.setDescricao(model.getDescricao());
			quarto.setNumero(model.getNumero());
			Arrays.stream(model.getDestinacoes())
					.collect(Collectors.toList())
					.forEach(id -> destinacaoHospedagemRepo.findById(id)
							.ifPresent( d -> quarto.getDestinacoes().add(d))
					);

			atomicQuarto.set(quartoRepo.save(quarto));
		});
		return atomicQuarto.get();
	}

	public List<Quarto> findAll() {
		return quartoRepo.findAllOrderByQuartoNumero();
	}

	public List<Quarto> findAllByDestinacaoHospedagem(Long id) {
		return quartoRepo.findByDestinacaoHospedagemId(id);
	}

	public List<Leito> findLeitosByQuarto(Long quartoId) {
		Optional<Quarto> q = quartoRepo.findById(quartoId);
		return q.isPresent() ? leitoRepo.findByQuartoId(q.get().getId()) : new ArrayList<>();
	}

	public List<Leito> findLeitosDisponiveis() {
		return leitoRepo.findAllWhereDisponivel(LogicoEnum.S);
	}

	public List<SelectValueVO> listTipoLeito() {
		return tipoLeitoRepo.findAllOrderByDescricao()
				.stream()
				.map(t -> new SelectValueVO(t.getId(), t.getDescricao()))
				.collect(Collectors.toList());
	}

	public boolean existeOutroLeitoComEsseNumero(Long leitoId, Long quartoId, Integer numero) {
		return !quartoRepo.existeOutroLeitoComEsseNumero(leitoId, quartoId, numero).isEmpty();
	}

	public boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero) {
		return !quartoRepo.existeOutroLeitoComEsseNumero(quartoId, numero).isEmpty();
	}

	public boolean existeOutroQuartoComEsseNumero(Long id, Integer numero) {
		return !quartoRepo.existeOutroQuartoComEsseNumero(id, numero).isEmpty();
	}

	public boolean existeOutroQuartoComEsseNumero(Integer numero) {
		return !quartoRepo.existeOutroQuartoComEsseNumero(numero).isEmpty();
	}

	private TipoLeito findTipoLeitoById(Long id){
		Optional<TipoLeito> opt = tipoLeitoRepo.findById(id);
		return opt.orElse(null);
	}

	private SituacaoLeito findSituacaoLeitoById(Long id){
		Optional<SituacaoLeito> opt = situacaoLeitoRepo.findById(id);
		return opt.orElse(null);
	}

	private Quarto findQuartoById(Long id) {
		Optional<Quarto> opt = quartoRepo.findById(id);
		return opt.orElse(null);
	}

}
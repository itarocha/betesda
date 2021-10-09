package br.com.itarocha.betesda.core.service;

import br.com.itarocha.betesda.core.ports.out.EntidadeRepository;
import br.com.itarocha.betesda.core.ports.in.EntidadeUseCase;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import br.com.itarocha.betesda.util.validacoes.Violation;
import br.com.itarocha.betesda.utils.Validadores;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Transactional
public class EntidadeService implements EntidadeUseCase {

	private final EntidadeRepository repositorio;
	private final ValidatorUtil validationUtils;

	@Override
	public Entidade create(Entidade model) {
		Set<Violation> violations = new HashSet<>();

		// TODO - Não entrarão mais valores com máscara. Remover esses replaces
		if (model.getCnpj() != null) {
			model.setCnpj(model.getCnpj().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
		}

		if (model.getCnpj() != null && model.getCnpj() != "") {
			if (!Validadores.isValidCNPJ(model.getCnpj())) {
				violations.add(new Violation("cnpj", "CNPJ inválido"));
			}
		}
		if (!repositorio.existeOutraEntidadeComEsseCNPJ(model.getId(), model.getCnpj()).isEmpty()) {
			violations.add(new Violation("cnpj", "CNPJ já casdastrado para outra Entidade"));
		}
		if (!violations.isEmpty()){
			throw new EntityValidationException(violations);
		}

		if (model.getEndereco() != null && model.getEndereco().getCep() != null) {
			model.getEndereco().setCep((model.getEndereco().getCep().replaceAll("\\-", "")));
		}

		validationUtils.validate(model);

		try {
			return repositorio.save(model);
		} catch (Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@Override
	public void remove(Long id) {
		repositorio.deleteById(id);
	}

	@Override
	public Entidade update(Entidade model) {
		Entidade obj = find(model.getId());
		if (!isNull(obj)) {
			return repositorio.save(model);
		}
		return model;
	}

	@Override
	public Entidade find(Long id) {
		return repositorio.findById(id).orElse(null);
	}

	@Override
	public List<Entidade> findAll() {
		return repositorio.findAllOrderByNome();
	}

	@Override
	public List<Entidade> consultar(String texto) {
		return repositorio.findAllLikeNomeLowerCase(texto);
	}

	@Override
	public List<ItemDictionary> listSelect() {
		return repositorio.findAllToSelectVO();
	}

}

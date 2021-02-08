package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.application.out.EnderecoRepository;
import br.com.itarocha.betesda.application.out.EntidadeRepository;
import br.com.itarocha.betesda.application.port.in.EntidadeUseCase;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.domain.SelectValueVO;
import br.com.itarocha.betesda.util.validacoes.EntityValidationException;
import br.com.itarocha.betesda.util.validacoes.Violation;
import br.com.itarocha.betesda.utils.Validadores;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

import static java.util.Objects.isNull;

@Service
@Transactional
@RequiredArgsConstructor
public class EntidadeService implements EntidadeUseCase {

	private final EntidadeRepository repositorio;
	private final EnderecoRepository enderecoRepository;

	@Override
	public Entidade create(Entidade model) {
		Set<Violation> violations = new HashSet<>();

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

		if (model.getCnpj() != null) {
			model.setCnpj(model.getCnpj().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
		}
		if (model.getEndereco() != null && model.getEndereco().getCep() != null) {
			model.getEndereco().setCep((model.getEndereco().getCep().replaceAll("\\-", "")));
		}

		try {
			enderecoRepository.save(model.getEndereco());
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
	public List<SelectValueVO> listSelect() {
		return repositorio.findAllToSelectVO();
	}

}

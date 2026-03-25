package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.PessoaPort;
import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.mapper.PessoaMapper;
import br.com.itarocha.betesda.core.domain.model.SearchRequest;
import br.com.itarocha.betesda.adapters.in.rest.request.PessoaRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.PessoaResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.PessoaEntity;
import br.com.itarocha.betesda.core.utils.Validadores;
import br.com.itarocha.betesda.core.validation.ResultError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/pessoas")
@RequiredArgsConstructor
public class PessoasController {

	private final PessoaPort service;
	private final PessoaMapper mapper;
	
	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		Optional<PessoaEntity> model = service.find(id);
		if (model.isPresent()) {
			PessoaResponse resposta = mapper.toResponse(model.get());
			return new ResponseEntity<>(resposta, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("não encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/filtrar", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarComCriterio(@RequestBody SearchRequest search) {
		
		List<PessoaEntity> lista = new ArrayList<>();
		if (search.getValue().length() >= 3) {
			lista = service.findByFieldNameAndValue(search.getFieldName(), "%"+search.getValue()+"%");
		}
		List<PessoaResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}
	
	@Deprecated
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<PessoaEntity> lista = service.findByFieldNameAndValue("nome", "%MAR%");
		List<PessoaResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<PessoaEntity> lista = service.consultar(texto);
		List<PessoaResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@Valid @RequestBody PessoaRequest request) {
		if (request.getCartaoSus() != null) {
			request.setCartaoSus(request.getCartaoSus().replaceAll("\\.", ""));
		}
		if (request.getCpf() != null) {
			request.setCpf(request.getCpf().replaceAll("\\.", "").replaceAll("\\-", ""));
		}
		if (request.getEndereco() != null && request.getEndereco().getCep() != null) {
			request.getEndereco().setCep((request.getEndereco().getCep().replaceAll("\\-", "")));
		}
		
		ResultError errors = new ResultError();
		
		if (request.getCpf() != null && request.getCpf() != "") {
			if (!Validadores.isValidCPF(request.getCpf())) {
				errors.addError("cpf", "CPF inválido");
			}
		}
		
		if (!errors.getErrors().isEmpty()) {
			throw new ValidationException(errors);
		}
		
		try {
			PessoaEntity entity = mapper.toEntity(request);
			PessoaEntity saved = service.create(entity);
			PessoaResponse resposta = mapper.toResponse(saved);
		    return new ResponseEntity<PessoaResponse>(resposta, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}

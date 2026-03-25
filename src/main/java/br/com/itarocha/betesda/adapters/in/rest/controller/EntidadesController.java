package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.EntidadePort;
import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.mapper.EntidadeMapper;
import br.com.itarocha.betesda.adapters.in.rest.request.EntidadeRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.EntidadeResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EntidadeEntity;
import br.com.itarocha.betesda.core.utils.Validadores;
import br.com.itarocha.betesda.core.validation.ResultError;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/entidades")
@RequiredArgsConstructor
public class EntidadesController {

	private final EntidadePort service;
	private final EntidadeMapper mapper;

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		Optional<EntidadeEntity> model = service.find(id);
		if (model.isPresent()) {
			EntidadeResponse resposta = mapper.toResponse(model.get());
			return new ResponseEntity<>(resposta, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("não encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<EntidadeEntity> lista = service.findAll();
		List<EntidadeResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<EntidadeEntity> lista = service.consultar(texto);
		List<EntidadeResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@Valid @RequestBody EntidadeRequest request) {
		
		if (request.getCnpj() != null) {
			request.setCnpj(request.getCnpj().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
		}
		if (request.getEndereco() != null && request.getEndereco().getCep() != null) {
			request.getEndereco().setCep(request.getEndereco().getCep().replaceAll("\\-", ""));
		}
		
		ResultError errors = new ResultError();
		
		if (request.getCnpj() != null && request.getCnpj() != "") {
			if (!Validadores.isValidCNPJ(request.getCnpj())) {
				errors.addError("cnpj", "CNPJ inválido");
			}
		}
		
		if (!errors.getErrors().isEmpty()) {
			throw new ValidationException(errors);
		}
		
		try {
			EntidadeEntity entity = mapper.toEntity(request);
			EntidadeEntity saved = service.create(entity);
			EntidadeResponse resposta = mapper.toResponse(saved);
		    return new ResponseEntity<>(resposta, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}

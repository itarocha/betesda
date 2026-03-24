package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.TipoHospedePort;
import br.com.itarocha.betesda.mapper.TipoHospedeMapper;
import br.com.itarocha.betesda.adapters.in.rest.request.TipoHospedeRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.TipoHospedeResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoHospedeEntity;
import br.com.itarocha.betesda.core.services.TipoHospedeService;
import br.com.itarocha.betesda.core.validation.ItaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/tipo_hospede")
@RequiredArgsConstructor
public class TipoHospedeController {

	private final TipoHospedePort service;
	private final TipoHospedeMapper mapper;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<TipoHospedeEntity> lista = service.findAll();
		List<TipoHospedeResponse> resposta = mapper.toResponseList(lista);
	    return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			TipoHospedeEntity model = service.find(id);
			if (model != null) {
				TipoHospedeResponse resposta = mapper.toResponse(model);
				return new ResponseEntity<>(resposta, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Tipo de Hóspede não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody TipoHospedeRequest request) {
		ItaValidator<TipoHospedeRequest> v = new ItaValidator<TipoHospedeRequest>(request);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			TipoHospedeEntity entity = mapper.toEntity(request);
			TipoHospedeEntity saved = service.create(entity);
			TipoHospedeResponse resposta = mapper.toResponse(saved);
		    return new ResponseEntity<>(resposta, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
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
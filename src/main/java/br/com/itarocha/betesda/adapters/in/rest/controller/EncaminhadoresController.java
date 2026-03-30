package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.EncaminhadorPort;
import br.com.itarocha.betesda.mapper.EncaminhadorMapper;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.in.rest.request.EncaminhadorRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.EncaminhadorResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.EncaminhadorEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/encaminhadores")
@RequiredArgsConstructor
public class EncaminhadoresController {

	private final EncaminhadorPort service;
	private final EncaminhadorMapper mapper;
	
	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		Optional<EncaminhadorEntity> model = service.find(id);
		if (model.isPresent()) {
			EncaminhadorResponse resposta = mapper.toResponse(model.get());
			return new ResponseEntity<>(resposta, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("não encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value="/por_encaminhador/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar(@PathVariable("id") Long entidadeId) {
		List<EncaminhadorEntity> lista = service.findAll(entidadeId);
		List<EncaminhadorResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@Valid @RequestBody EncaminhadorRequest request) {
		try {
			EncaminhadorEntity entity = mapper.toEntity(request);
			EncaminhadorEntity saved = service.create(entity);
			EncaminhadorResponse resposta = mapper.toResponse(saved);
		    return new ResponseEntity<>(resposta, HttpStatus.OK);
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
	
	@RequestMapping("/lista/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getListaEncaminhadores(@PathVariable("id") Long entidadeId) {
		List<ValorTexto> lista = service.listSelect(entidadeId);
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}
	
}

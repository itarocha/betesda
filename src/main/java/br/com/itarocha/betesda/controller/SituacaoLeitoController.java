package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.mapper.SituacaoLeitoMapper;
import br.com.itarocha.betesda.model.request.SituacaoLeitoRequest;
import br.com.itarocha.betesda.model.response.SituacaoLeitoResponse;
import br.com.itarocha.betesda.persistencia.model.SituacaoLeitoEntity;
import br.com.itarocha.betesda.service.SituacaoLeitoService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/situacao_leito")
@RequiredArgsConstructor
public class SituacaoLeitoController {

	private final SituacaoLeitoService service;
	private final SituacaoLeitoMapper mapper;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<SituacaoLeitoEntity> lista = service.findAll();
		List<SituacaoLeitoResponse> resposta = mapper.toResponseList(lista);
	    return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			SituacaoLeitoEntity model = service.find(id);
			if (model != null) {
				SituacaoLeitoResponse resposta = mapper.toResponse(model);
				return new ResponseEntity<>(resposta, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Situação de Leito não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody SituacaoLeitoRequest request) {
		ItaValidator<SituacaoLeitoRequest> v = new ItaValidator<SituacaoLeitoRequest>(request);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			SituacaoLeitoEntity entity = mapper.toEntity(request);
			SituacaoLeitoEntity saved = service.create(entity);
			SituacaoLeitoResponse resposta = mapper.toResponse(saved);
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
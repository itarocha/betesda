package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.SituacaoLeitoPort;
import br.com.itarocha.betesda.mapper.SituacaoLeitoMapper;
import br.com.itarocha.betesda.adapters.in.rest.request.SituacaoLeitoRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.SituacaoLeitoResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.SituacaoLeitoEntity;
import jakarta.validation.Valid;
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

	private final SituacaoLeitoPort service;
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
	public ResponseEntity<?> gravar(@Valid @RequestBody SituacaoLeitoRequest request) {
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

package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.DestinacaoHospedagemPort;
import br.com.itarocha.betesda.mapper.DestinacaoHospedagemMapper;
import br.com.itarocha.betesda.adapters.in.rest.request.DestinacaoHospedagemRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.DestinacaoHospedagemResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.DestinacaoHospedagemEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/destinacao_hospedagem")
@RequiredArgsConstructor
public class DestinacaoHospedagemController {

	private final DestinacaoHospedagemPort service;
	private final DestinacaoHospedagemMapper mapper;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<DestinacaoHospedagemEntity> lista = service.findAll();
		List<DestinacaoHospedagemResponse> resposta = mapper.toResponseList(lista);
	    return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			DestinacaoHospedagemEntity model = service.find(id);
			if (model != null) {
				DestinacaoHospedagemResponse resposta = mapper.toResponse(model);
				return new ResponseEntity<>(resposta, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Destinação de Hospedagem não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@Valid @RequestBody DestinacaoHospedagemRequest request) {
		try {
			DestinacaoHospedagemEntity entity = mapper.toEntity(request);
			DestinacaoHospedagemEntity saved = service.create(entity);
			DestinacaoHospedagemResponse resposta = mapper.toResponse(saved);
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

package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.ports.out.TipoLeitoPort;
import br.com.itarocha.betesda.mapper.TipoLeitoMapper;
import br.com.itarocha.betesda.adapters.in.rest.request.TipoLeitoRequest;
import br.com.itarocha.betesda.adapters.in.rest.response.TipoLeitoResponse;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.TipoLeitoEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/tipo_leito")
@RequiredArgsConstructor
public class TipoLeitoController {

	private final TipoLeitoPort service;
	private final TipoLeitoMapper mapper;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<TipoLeitoEntity> lista = service.findAll();
		List<TipoLeitoResponse> resposta = mapper.toResponseList(lista);
	    return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			TipoLeitoEntity model = service.find(id);
			if (model != null) {
				TipoLeitoResponse resposta = mapper.toResponse(model);
				return new ResponseEntity<>(resposta, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Tipo de Leito não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@Valid @RequestBody TipoLeitoRequest request) {
		try {
			TipoLeitoEntity entity = mapper.toEntity(request);
			TipoLeitoEntity saved = service.create(entity);
			TipoLeitoResponse resposta = mapper.toResponse(saved);
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

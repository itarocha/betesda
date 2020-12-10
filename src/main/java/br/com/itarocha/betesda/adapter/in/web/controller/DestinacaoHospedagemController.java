package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.application.port.in.DestinacaoHospedagemUseCase;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.util.validation.ResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/destinacao_hospedagem")
@RequiredArgsConstructor
public class DestinacaoHospedagemController {

	private final DestinacaoHospedagemUseCase service;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
	    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			DestinacaoHospedagem model = service.find(id);
			if (model != null) {
				return new ResponseEntity<>(model, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Destinação de Hospedagem não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody DestinacaoHospedagem model) {
		ItaValidator<DestinacaoHospedagem> v = new ItaValidator(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<ResultError>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
		    return new ResponseEntity(service.create(model), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
		    return new ResponseEntity("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
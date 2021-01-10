package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.application.port.in.DestinacaoHospedagemUseCase;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.util.validation.EntityValidationError;
import br.com.itarocha.betesda.util.validation.StaticValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;

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
				return ResponseEntity.ok(model);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(consumes =  MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody DestinacaoHospedagem model) {
		EntityValidationError re = StaticValidator.validate(model);
		if (!re.hasErrors() ) {
			return ResponseEntity.unprocessableEntity().body(new ApiError(re.getErrors()));
		}

		try {
			return ResponseEntity.ok(service.create(model));
		} catch (ConstraintViolationException e){
			return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
		} catch (Exception e) {
			return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
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
package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.application.TipoHospedeService;
import br.com.itarocha.betesda.domain.TipoHospede;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/tipo_hospede")
public class TipoHospedeController {

	@Autowired
	private TipoHospedeService service;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
	    return new ResponseEntity(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		TipoHospede model = service.find(id);
		if (model != null) {
			return ResponseEntity.ok(model);
		} else {
			return ResponseEntity.notFound().build();
		}
	 }
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody TipoHospede model) {
		ItaValidator<TipoHospede> v = new ItaValidator<TipoHospede>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return ResponseEntity.unprocessableEntity()
					.body(new ApiError(v.getValidationResult().getErrors()));
		}
		
		try {
		    return new ResponseEntity(service.create(model), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new ApiError(e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
		    return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity(	new ApiError(e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}

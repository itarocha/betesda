package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.application.TipoServicoService;
import br.com.itarocha.betesda.domain.TipoServico;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/tipo_servico")
@RequiredArgsConstructor
public class TipoServicoController {

	private final TipoServicoService service;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
	    return ResponseEntity.ok(service.findAll());
	}

	@GetMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		TipoServico model = service.find(id);
		if (model != null) {
			return ResponseEntity.ok(model);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody TipoServico model) {
		ItaValidator<TipoServico> v = new ItaValidator(model);
		v.validate();
		if (!v.hasErrors() ) {
			return ResponseEntity.unprocessableEntity()
					.body(new ApiError(v.getValidationResult().getErrors()));
		}
		
		try {
		    return ResponseEntity.ok(service.create(model));
		} catch (Exception e) {
			return new ResponseEntity(	new ApiError(e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value="{id}")
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

package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.core.ports.in.TipoHospedeUseCase;
import br.com.itarocha.betesda.domain.TipoHospede;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/app/tipo_hospede")
@RequiredArgsConstructor
public class TipoHospedeController {

	private final TipoHospedeUseCase service;
	private final ValidatorUtil validationUtils;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<TipoHospede>> listar() {
	    return new ResponseEntity(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<TipoHospede> getById(@PathVariable("id") Long id) {
		TipoHospede model = service.find(id);
		return Objects.nonNull(model) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
	 }
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<TipoHospede> gravar(@RequestBody TipoHospede model) {
		validationUtils.validate(model);
		return ResponseEntity.ok(service.create(model));
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	 }
}

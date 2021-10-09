package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.core.ports.in.TipoServicoUseCase;
import br.com.itarocha.betesda.domain.TipoServico;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/app/tipo_servico")
@RequiredArgsConstructor
public class TipoServicoController {

	private final TipoServicoUseCase service;
	private final ValidatorUtil validationUtils;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<TipoServico>> listar() {
	    return ResponseEntity.ok(service.findAll());
	}

	@GetMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<TipoServico> getById(@PathVariable("id") Long id) {
		TipoServico model = service.find(id);
		return Objects.nonNull(model) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<TipoServico> gravar(@RequestBody TipoServico model) {
		validationUtils.validate(model);
		return ResponseEntity.ok(service.create(model));
	}
	
	@DeleteMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	 }
}

package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.core.ports.in.SituacaoLeitoUseCase;
import br.com.itarocha.betesda.domain.SituacaoLeito;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/app/situacao_leito")
@RequiredArgsConstructor
public class SituacaoLeitoController {

	private final SituacaoLeitoUseCase service;
	private final ValidatorUtil validationUtils;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<SituacaoLeito>> listar() {
	    return new ResponseEntity(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<SituacaoLeito> getById(@PathVariable("id") Long id) {
		SituacaoLeito model = service.find(id);
		return Objects.nonNull(model) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
	 }
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<SituacaoLeito> gravar(@RequestBody SituacaoLeito model) {
		validationUtils.validate(model);;
		return ResponseEntity.ok(service.create(model));
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	 }
}
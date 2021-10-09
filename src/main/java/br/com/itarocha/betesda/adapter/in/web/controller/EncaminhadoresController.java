package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.core.ports.in.EncaminhadorUseCase;
import br.com.itarocha.betesda.domain.Encaminhador;
import br.com.itarocha.betesda.domain.ItemDictionary;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/encaminhadores")
@RequiredArgsConstructor
public class EncaminhadoresController {

	private final EncaminhadorUseCase service;
	private final ValidatorUtil validationUtils;

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<Encaminhador> getById(@PathVariable("id") Long id) {
		Optional<Encaminhador> opt = service.find(id);
		return opt.isPresent() ? ResponseEntity.ok(opt.get()) : ResponseEntity.notFound().build();
	}

	@GetMapping("/por_encaminhador/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Encaminhador>> listar(@PathVariable("id") Long entidadeId) {
		return ResponseEntity.ok(service.findAllByEntidadeId(entidadeId));
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<Encaminhador> gravar(@RequestBody Encaminhador model) {
		validationUtils.validate(model);
		return ResponseEntity.ok(service.create(model));
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		service.remove(id);
		return ResponseEntity.noContent().build();
	 }
	
	@GetMapping("/lista/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<ItemDictionary>> getListaEncaminhadores(@PathVariable("id") Long entidadeId) {
		return ResponseEntity.ok(service.listSelectByEntidadeId(entidadeId));
	}
}

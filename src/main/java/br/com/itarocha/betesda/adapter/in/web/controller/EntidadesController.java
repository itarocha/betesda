package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.domain.Entidade;
import br.com.itarocha.betesda.application.EntidadeService;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/entidades")
@RequiredArgsConstructor
public class EntidadesController {

	private final EntidadeService service;
	private final ValidatorUtil validationUtils;
	
	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<Entidade> getById(@PathVariable("id") Long id) {
		Entidade model = service.find(id);
		if (model != null) {
			return ResponseEntity.ok(model);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Entidade>> listar() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping(value = "/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Entidade>> consultar(@PathVariable("texto") String texto) {
		return ResponseEntity.ok(service.consultar(texto));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody Entidade model) {
		return ResponseEntity.ok(service.create(model));
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
		}
	 }
}
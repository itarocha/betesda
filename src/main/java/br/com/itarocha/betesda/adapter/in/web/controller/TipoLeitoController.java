package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.application.TipoLeitoService;
import br.com.itarocha.betesda.domain.TipoLeito;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/app/tipo_leito")
@RequiredArgsConstructor
public class TipoLeitoController {

	private final TipoLeitoService service;
	private final ValidatorUtil validationUtils;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<TipoLeito>> listar() {
	    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<TipoLeito> getById(@PathVariable("id") Long id) {
		TipoLeito model = service.find(id);
		return Objects.nonNull(model) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<TipoLeito> gravar(@RequestBody TipoLeito model) {
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

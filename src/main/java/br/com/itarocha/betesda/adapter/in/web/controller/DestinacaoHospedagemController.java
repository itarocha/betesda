package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.application.port.in.DestinacaoHospedagemUseCase;
import br.com.itarocha.betesda.domain.DestinacaoHospedagem;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/app/destinacao_hospedagem")
@RequiredArgsConstructor
public class DestinacaoHospedagemController {

	private final DestinacaoHospedagemUseCase service;
	private final ValidatorUtil validationUtils;

	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<DestinacaoHospedagem>> listar() {
	    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	@GetMapping("{id}")
	public ResponseEntity<DestinacaoHospedagem> getById(@PathVariable("id") Long id) {
		DestinacaoHospedagem model = service.find(id);
		return Objects.nonNull(model) ? ResponseEntity.ok(model) : ResponseEntity.notFound().build();
	}
	
	@PostMapping(consumes =  MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<DestinacaoHospedagem> gravar(@RequestBody DestinacaoHospedagem model) {
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
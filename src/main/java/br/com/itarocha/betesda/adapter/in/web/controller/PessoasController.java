package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.core.ports.in.PessoaUseCase;
import br.com.itarocha.betesda.domain.Pessoa;
import br.com.itarocha.betesda.domain.SearchRequest;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/pessoas")
@RequiredArgsConstructor
public class PessoasController {

	private final PessoaUseCase service;
	private final ValidatorUtil validationUtils;

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<Pessoa> getById(@PathVariable("id") Long id) {
		Optional<Pessoa> opt = service.find(id);

		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/filtrar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Pessoa>> listarComCriterio(@RequestBody SearchRequest search) {
		//TODO implementar busca por campo....
		List<Pessoa> lista = new ArrayList<>();
		if (search.getValue().length() >= 3) {
			//lista = service.findByFieldNameAndValue(search.getFieldName(), "%"+search.getValue()+"%");
			lista = service.findAllLikeNomeLowerCase(search.getValue());
		}
		
		return ResponseEntity.ok(lista);
	}

	/*
	@Deprecated
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		//List<Pessoa> lista = service.findAll();
		
		List<PessoaEntity> lista = service.findByFieldNameAndValue("nome", "%MAR%");
		//List<Pessoa> lista = service.findByFieldNameAndValue("cpf", "%282%");
		return new ResponseEntity<List<PessoaEntity>>(lista, HttpStatus.OK);
	}
	*/

	@GetMapping("/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		return ResponseEntity.ok(service.findAllLikeNomeLowerCase(texto));
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody Pessoa model) {
		return ResponseEntity.ok(service.create(model));
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
		}
	 }
}

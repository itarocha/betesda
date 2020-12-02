package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.out.persistence.entity.DestinacaoHospedagemEntity;
import br.com.itarocha.betesda.application.DestinacaoHospedagemService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/destinacao_hospedagem")
public class DestinacaoHospedagemController {

	@Autowired
	private DestinacaoHospedagemService service;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<DestinacaoHospedagemEntity> lista = service.findAll();
	    return new ResponseEntity<List<DestinacaoHospedagemEntity>>(lista, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	@GetMapping("{id}")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			DestinacaoHospedagemEntity model = service.find(id);
			if (model != null) {
				return new ResponseEntity<>(model, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Destinação de Hospedagem não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody DestinacaoHospedagemEntity model) {
		ItaValidator<DestinacaoHospedagemEntity> v = new ItaValidator<DestinacaoHospedagemEntity>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			DestinacaoHospedagemEntity saved = null;
			saved = service.create(model);
		    return new ResponseEntity<DestinacaoHospedagemEntity>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.model.EncaminhadorEntity;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.service.EncaminhadorService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/encaminhadores")
public class EncaminhadoresController {

	@Autowired
	private EncaminhadorService service;
	
	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			Optional<EncaminhadorEntity> model = service.find(id);
			if (model.isPresent()) {
				return new ResponseEntity<EncaminhadorEntity>(model.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("não encontrado", HttpStatus.NOT_FOUND);
			}
		} finally {
			//em.close();
		}
	}

	@RequestMapping(value="/por_encaminhador/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar(@PathVariable("id") Long entidadeId) {
		List<EncaminhadorEntity> lista = service.findAll(entidadeId);
		return new ResponseEntity<List<EncaminhadorEntity>>(lista, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody EncaminhadorEntity model) {
		ItaValidator<EncaminhadorEntity> v = new ItaValidator<EncaminhadorEntity>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			EncaminhadorEntity saved = null;
			saved = service.create(model);
		    return new ResponseEntity<EncaminhadorEntity>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping("/lista/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getListaEncaminhadores(@PathVariable("id") Long entidadeId) {
		List<SelectValueVO> lista = service.listSelect(entidadeId);
		return new ResponseEntity<List<SelectValueVO>>(lista, HttpStatus.OK);
	}
	
}

package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.domain.Pessoa;
import br.com.itarocha.betesda.model.SearchRequest;
import br.com.itarocha.betesda.service.PessoaService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.utils.Validadores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/pessoas")
public class PessoasController {

	@Autowired
	private PessoaService service;
	
	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			Optional<Pessoa> model = service.find(id);
			if (model.isPresent()) {
				return new ResponseEntity<Pessoa>(model.get(), HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("não encontrado", HttpStatus.NOT_FOUND);
			}
		} finally {
			//em.close();
		}
	}

	@PostMapping("/filtrar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarComCriterio(@RequestBody SearchRequest search) {
		
		List<Pessoa> lista = new ArrayList<>();
		if (search.getValue().length() >= 3) {
			lista = service.findByFieldNameAndValue(search.getFieldName(), "%"+search.getValue()+"%");
		}
		
		//List<Pessoa> lista = service.findByFieldNameAndValue("cpf", "%282%");
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}
	
	@Deprecated
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		//List<Pessoa> lista = service.findAll();
		
		List<Pessoa> lista = service.findByFieldNameAndValue("nome", "%MAR%");
		//List<Pessoa> lista = service.findByFieldNameAndValue("cpf", "%282%");
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<Pessoa> lista = service.consultar(texto);
		return new ResponseEntity<List<Pessoa>>(lista, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody Pessoa model) {
		// TODO Criar classe de conversores de cep, sus, cpf, etc para remover máscara
		if (model.getCartaoSus() != null) {
			model.setCartaoSus(model.getCartaoSus().replaceAll("\\.", ""));
		}
		if (model.getCpf() != null) {
			model.setCpf(model.getCpf().replaceAll("\\.", "").replaceAll("\\-", ""));
		}
		if (model.getEndereco() != null && model.getEndereco().getCep() != null) {
			model.getEndereco().setCep((model.getEndereco().getCep().replaceAll("\\-", "")));
		}
		
		ItaValidator<Pessoa> v = new ItaValidator<Pessoa>(model);
		v.validate();
		
		if (model.getCpf() != null && model.getCpf() != "") {
			if (!Validadores.isValidCPF(model.getCpf())) {
				v.addError("cpf", "CPF inválido");
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			Pessoa saved = null;
			saved = service.create(model);
		    return new ResponseEntity<Pessoa>(saved, HttpStatus.OK);
		} catch (ValidationException e) {
			ResponseEntity<?> re = new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST); 
			return re;
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

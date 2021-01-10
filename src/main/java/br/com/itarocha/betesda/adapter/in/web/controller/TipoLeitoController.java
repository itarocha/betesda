package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.application.TipoLeitoService;
import br.com.itarocha.betesda.domain.TipoLeito;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/tipo_leito")
public class TipoLeitoController {

	@Autowired
	private TipoLeitoService service;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
	    return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		TipoLeito model = service.find(id);
		if (model != null) {
			return new ResponseEntity(model, HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody TipoLeito model) {
		ItaValidator<TipoLeito> v = new ItaValidator<TipoLeito>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
		    return new ResponseEntity(service.create(model), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
		    return new ResponseEntity(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			// TODO: encapsular erro
			return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}

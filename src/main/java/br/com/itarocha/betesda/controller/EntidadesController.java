package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.mapper.EntidadeMapper;
import br.com.itarocha.betesda.mapper.EnderecoMapper;
import br.com.itarocha.betesda.model.request.EntidadeRequest;
import br.com.itarocha.betesda.model.response.EntidadeResponse;
import br.com.itarocha.betesda.persistencia.model.EntidadeEntity;
import br.com.itarocha.betesda.service.EntidadeService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.utils.Validadores;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/app/entidades")
@RequiredArgsConstructor
public class EntidadesController {

	private final EntidadeService service;
	private final EntidadeMapper mapper;
	private final EnderecoMapper enderecoMapper;
	
	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		Optional<EntidadeEntity> model = service.find(id);
		if (model.isPresent()) {
			EntidadeResponse resposta = mapper.toResponse(model.get());
			return new ResponseEntity<>(resposta, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("não encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<EntidadeEntity> lista = service.findAll();
		List<EntidadeResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<EntidadeEntity> lista = service.consultar(texto);
		List<EntidadeResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody EntidadeRequest request) {
		
		if (request.getCnpj() != null) {
			request.setCnpj(request.getCnpj().replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("\\/", ""));
		}
		if (request.getEndereco() != null && request.getEndereco().getCep() != null) {
			request.getEndereco().setCep(request.getEndereco().getCep().replaceAll("\\-", ""));
		}
		
		ItaValidator<EntidadeRequest> v = new ItaValidator<EntidadeRequest>(request);
		v.validate();
		
		if (request.getCnpj() != null && request.getCnpj() != "") {
			if (!Validadores.isValidCNPJ(request.getCnpj())) {
				v.addError("cnpj", "CNPJ inválido");
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			EntidadeEntity entity = mapper.toEntity(request);
			EntidadeEntity saved = service.create(entity);
			EntidadeResponse resposta = mapper.toResponse(saved);
		    return new ResponseEntity<>(resposta, HttpStatus.OK);
		} catch (ValidationException e) {
			ResponseEntity<?> re = new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST); 
			return re;
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.mapper.PessoaMapper;
import br.com.itarocha.betesda.model.SearchRequest;
import br.com.itarocha.betesda.model.request.PessoaRequest;
import br.com.itarocha.betesda.model.response.PessoaResponse;
import br.com.itarocha.betesda.persistencia.model.PessoaEntity;
import br.com.itarocha.betesda.service.PessoaService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.utils.Validadores;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

	private final PessoaService service;
	private final PessoaMapper mapper;
	
	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		Optional<PessoaEntity> model = service.find(id);
		if (model.isPresent()) {
			PessoaResponse resposta = mapper.toResponse(model.get());
			return new ResponseEntity<>(resposta, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("não encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/filtrar", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarComCriterio(@RequestBody SearchRequest search) {
		
		List<PessoaEntity> lista = new ArrayList<>();
		if (search.getValue().length() >= 3) {
			lista = service.findByFieldNameAndValue(search.getFieldName(), "%"+search.getValue()+"%");
		}
		List<PessoaResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}
	
	@Deprecated
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<PessoaEntity> lista = service.findByFieldNameAndValue("nome", "%MAR%");
		List<PessoaResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/consultar/{texto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> consultar(@PathVariable("texto") String texto) {
		List<PessoaEntity> lista = service.consultar(texto);
		List<PessoaResponse> resposta = mapper.toResponseList(lista);
		return new ResponseEntity<>(resposta, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody PessoaRequest request) {
		if (request.getCartaoSus() != null) {
			request.setCartaoSus(request.getCartaoSus().replaceAll("\\.", ""));
		}
		if (request.getCpf() != null) {
			request.setCpf(request.getCpf().replaceAll("\\.", "").replaceAll("\\-", ""));
		}
		if (request.getEndereco() != null && request.getEndereco().getCep() != null) {
			request.getEndereco().setCep((request.getEndereco().getCep().replaceAll("\\-", "")));
		}
		
		ItaValidator<PessoaRequest> v = new ItaValidator<>(request);
		v.validate();
		
		if (request.getCpf() != null && request.getCpf() != "") {
			if (!Validadores.isValidCPF(request.getCpf())) {
				v.addError("cpf", "CPF inválido");
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			PessoaEntity entity = mapper.toEntity(request);
			PessoaEntity saved = service.create(entity);
			PessoaResponse resposta = mapper.toResponse(saved);
		    return new ResponseEntity<PessoaResponse>(resposta, HttpStatus.OK);
		} catch (ValidationException e) {
			ResponseEntity<?> re = new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST); 
			return re;
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
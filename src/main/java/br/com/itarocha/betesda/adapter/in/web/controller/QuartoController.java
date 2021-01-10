package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.application.*;
import br.com.itarocha.betesda.application.port.in.*;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/app/quarto")
public class QuartoController {

	@Autowired
	private QuartoUseCase service;

	@Autowired
	private TipoLeitoUseCase tls;

	@Autowired
	private DestinacaoHospedagemUseCase dhs;

	@Autowired
	private SituacaoLeitoUseCase sls;

	@Autowired
	private TipoHospedeUseCase ths;

	@Autowired
	private TipoServicoUseCase tss;
	
	@Autowired
	private EntidadeService etds;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity listar() {
		List<Quarto> lista = service.findAll();
		return new ResponseEntity(lista, HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity getById(@PathVariable("id") Long id) {
		Quarto model = service.find(id);
		if (model != null) {
			return new ResponseEntity(model, HttpStatus.OK);
		} else {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity getLeitoById(@PathVariable("id") Long id) {
		try {
			Leito model = service.findLeito(id);
			if (model != null) {
				EditLeitoVO leito = new EditLeitoVO();
				leito.setId(model.getId());
				leito.setNumero(model.getNumero());
				leito.setQuartoId(model.getQuarto().getId());
				leito.setQuartoNumero(model.getQuarto().getNumero());
				leito.setTipoLeito(model.getTipoLeito().getId());
				leito.setSituacao(model.getSituacao().getId());
				
				return ResponseEntity.ok(leito);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return new ResponseEntity(new ApiError(e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/por_destinacao_hospedagem/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity listarByDestinacaoHospedagem(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findAllByDestinacaoHospedagem(id));
	}

	@GetMapping("/{id}/leitos")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Leito>> listarLeitosByQuarto(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findLeitosByQuarto(id));
	}

	@GetMapping("/leitos_disponiveis")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity listarLeitosDisponiveis() {
		return ResponseEntity.ok(service.findLeitosDisponiveis());
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity gravar(@RequestBody QuartoNew model) throws Exception {
		ItaValidator<QuartoNew> v = new ItaValidator<QuartoNew>(model);
		v.validate();
		if (service.existeOutroQuartoComEsseNumero(model.getNumero())) {
			v.addError("numero", "Existe outro Quarto com esse número");
		}
		
		if (!v.hasErrors() ) {
			return ResponseEntity.unprocessableEntity().body(new ApiError(v.getValidationResult().getErrors()));
		}
		return ResponseEntity.ok(service.create(model));
	}
	
	@PostMapping("/alterar")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity gravarAlteracao(@RequestBody QuartoEdit model) {
		ItaValidator<QuartoEdit> v = new ItaValidator<QuartoEdit>(model);
		v.validate();
		try {
			if (model.getId() != null) {
				if (service.existeOutroQuartoComEsseNumero(model.getId(), model.getNumero())) {
					v.addError("numero", "Existe outro Quarto com esse número");
				}
			}
			
			if (!v.hasErrors() ) {
				return ResponseEntity.unprocessableEntity().body(new ApiError(v.getValidationResult().getErrors()));
			}
		
			Quarto saved = null;
			saved = service.update(model);
		    return new ResponseEntity<Quarto>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<ApiError>(new ApiError(e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/leito")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarLeito(@RequestBody EditLeitoVO model) {
		ItaValidator<EditLeitoVO> v = new ItaValidator<>(model);
		v.validate();
		
		try {
			if (model.getId() == null) {
				if (service.existeOutroLeitoComEsseNumero(model.getQuartoId(), model.getNumero())) {
					v.addError("numero", "Existe outro Leito com esse número");
				}
			} else {
				if (service.existeOutroLeitoComEsseNumero(model.getId(), model.getQuartoId(), model.getNumero())) {
					v.addError("numero", "Existe outro Leito com esse número");
				}
			}
			
			if (!v.hasErrors() ) {
				return ResponseEntity.unprocessableEntity().body(new ApiError(v.getValidationResult().getErrors()));
			}
			return ResponseEntity.ok(service.saveLeito(model));
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new ApiError(e.getMessage()));
		}
	 }

	@DeleteMapping("/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluirLeito(@PathVariable("id") Long id) {
		try {
			service.removeLeito(id);
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return new ResponseEntity<ApiError>(new ApiError(e.getMessage()),
										HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@GetMapping("/listas")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<AutoWired> listas() {
		AutoWired retorno = new AutoWired();
		retorno.listaTipoLeito = tls.listSelect();
		retorno.listaDestinacaoHospedagem = dhs.listSelect();
		retorno.listaSituacaoLeito = sls.listSelect();
		retorno.listaTipoHospede = ths.listSelect();
		retorno.listaTipoServico = tss.listSelect();
		retorno.listaEntidade = etds.listSelect();
		return ResponseEntity.ok(retorno);
	}

	static class AutoWired {
		public List<SelectValueVO> listaTipoLeito = new ArrayList<>();
		public List<SelectValueVO> listaDestinacaoHospedagem = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaSituacaoLeito = new ArrayList<>();
		public List<SelectValueVO> listaTipoHospede = new ArrayList<>();
		public List<SelectValueVO> listaTipoServico = new ArrayList<>();
		public List<SelectValueVO> listaEntidade = new ArrayList<>();
	} 
}

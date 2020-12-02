package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.out.persistence.entity.LeitoEntity;
import br.com.itarocha.betesda.adapter.out.persistence.entity.QuartoEntity;
import br.com.itarocha.betesda.application.*;
import br.com.itarocha.betesda.domain.model.EditLeitoVO;
import br.com.itarocha.betesda.domain.model.EditQuartoVO;
import br.com.itarocha.betesda.domain.model.NovoQuartoVO;
import br.com.itarocha.betesda.domain.model.SelectValueVO;
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
	private QuartoService service;

	@Autowired
	private TipoLeitoService tls;

	@Autowired
	private DestinacaoHospedagemService dhs;
	
	@Autowired
	private SituacaoLeitoService sls;
	
	@Autowired
	private TipoHospedeService ths;
	
	@Autowired
	private TipoServicoService tss;
	
	@Autowired
	private EntidadeService etds;
	
	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<QuartoEntity> lista = service.findAll();
		return new ResponseEntity<List<QuartoEntity>>(lista, HttpStatus.OK);
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			QuartoEntity model = service.find(id);
			if (model != null) {
				return new ResponseEntity<>(model, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Quarto não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getLeitoById(@PathVariable("id") Long id) {
		try {
			LeitoEntity model = service.findLeito(id);
			if (model != null) {
				EditLeitoVO leito = new EditLeitoVO();
				leito.setId(model.getId());
				leito.setNumero(model.getNumero());
				leito.setQuartoId(model.getQuarto().getId());
				leito.setQuartoNumero(model.getQuarto().getNumero());
				leito.setTipoLeito(model.getTipoLeito().getId());
				leito.setSituacao(model.getSituacao().getId());
				
				return new ResponseEntity<>(leito, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Leito não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/por_destinacao_hospedagem/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarByDestinacaoHospedagem(@PathVariable("id") Long id) {
		List<QuartoEntity> lista = service.findAllByDestinacaoHospedagem(id);
		return new ResponseEntity<List<QuartoEntity>>(lista, HttpStatus.OK);
	}

	@GetMapping("/{id}/leitos")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarLeitosByQuarto(@PathVariable("id") Long id) {
		List<LeitoEntity> lista = service.findLeitosByQuarto(id);
		return new ResponseEntity<List<LeitoEntity>>(lista, HttpStatus.OK);
	}

	@GetMapping("/leitos_disponiveis")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarLeitosDisponiveis() {
		List<LeitoEntity> lista = service.findLeitosDisponiveis();
		return new ResponseEntity<List<LeitoEntity>>(lista, HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody NovoQuartoVO model) throws Exception {
		ItaValidator<NovoQuartoVO> v = new ItaValidator<NovoQuartoVO>(model);
		v.validate();
		if (service.existeOutroQuartoComEsseNumero(model.getNumero())) {
			v.addError("numero", "Existe outro Quarto com esse número");
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
	
		// TODO tratar exceção
		QuartoEntity saved = null;
		saved = service.create(model);
	    //return Response.status(200).entity(saved).build();
	    return new ResponseEntity<QuartoEntity>(saved, HttpStatus.OK);
	}
	
	@PostMapping("/alterar")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarAlteracao(@RequestBody EditQuartoVO model) {
		ItaValidator<EditQuartoVO> v = new ItaValidator<EditQuartoVO>(model);
		v.validate();
		try {
			if (model.getId() != null) {
				if (service.existeOutroQuartoComEsseNumero(model.getId(), model.getNumero())) {
					v.addError("numero", "Existe outro Quarto com esse número");
				}
			}
			
			if (!v.hasErrors() ) {
				return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
			}
		
			QuartoEntity saved = null;
			saved = service.update(model);
		    return new ResponseEntity<QuartoEntity>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/leito")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarLeito(@RequestBody EditLeitoVO model) {
		ItaValidator<EditLeitoVO> v = new ItaValidator<EditLeitoVO>(model);
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
				return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
			LeitoEntity saved = null;
			saved = service.saveLeito(model);
			return new ResponseEntity<LeitoEntity>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	 }

	@DeleteMapping("/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluirLeito(@PathVariable("id") Long id) {
		try {
			service.removeLeito(id);
			return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@GetMapping("/listas")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listas() {
		AutoWired retorno = new AutoWired();
		retorno.listaTipoLeito = tls.listSelect();
		
		retorno.listaDestinacaoHospedagem = dhs.listSelect();
		
		retorno.listaSituacaoLeito = sls.listSelect();
		
		retorno.listaTipoHospede = ths.listSelect();
		
		retorno.listaTipoServico = tss.listSelect();

		retorno.listaEntidade = etds.listSelect();
		
		return new ResponseEntity<AutoWired>(retorno, HttpStatus.OK);
	}

	static class AutoWired {
		public List<SelectValueVO> listaTipoLeito = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaDestinacaoHospedagem = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaSituacaoLeito = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaTipoHospede = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaTipoServico = new ArrayList<SelectValueVO>();
		public List<SelectValueVO> listaEntidade = new ArrayList<SelectValueVO>();
	} 
	
}

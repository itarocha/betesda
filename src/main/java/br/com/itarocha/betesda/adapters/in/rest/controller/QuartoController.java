package br.com.itarocha.betesda.adapters.in.rest.controller;

import br.com.itarocha.betesda.core.domain.model.LeitoEdicao;
import br.com.itarocha.betesda.core.domain.model.QuartoEdicao;
import br.com.itarocha.betesda.core.domain.model.QuartoNovo;
import br.com.itarocha.betesda.core.domain.model.ValorTexto;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.LeitoEntity;
import br.com.itarocha.betesda.adapters.out.persistencia.jpa.entity.QuartoEntity;
import br.com.itarocha.betesda.core.ports.out.*;
import br.com.itarocha.betesda.core.validation.ResultError;
import br.com.itarocha.betesda.exception.ValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/app/quarto")
@RequiredArgsConstructor
public class QuartoController {

	private final QuartoPort service;
	private final TipoLeitoPort tls;
	private final DestinacaoHospedagemPort dhs;
	private final SituacaoLeitoPort sls;
	private final TipoHospedePort ths;
	private final TipoServicoPort tss;
	private final EntidadePort etds;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<QuartoEntity> lista = service.findAll();
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
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
	
	@RequestMapping(value="/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getLeitoById(@PathVariable("id") Long id) {
		try {
			LeitoEntity model = service.findLeito(id);
			if (model != null) {
				LeitoEdicao leito = new LeitoEdicao();
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
	
	@RequestMapping("/por_destinacao_hospedagem/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarByDestinacaoHospedagem(@PathVariable("id") Long id) {
		List<QuartoEntity> lista = service.findAllByDestinacaoHospedagem(id);
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@RequestMapping("/{id}/leitos")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarLeitosByQuarto(@PathVariable("id") Long id) {
		List<LeitoEntity> lista = service.findLeitosByQuarto(id);
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@RequestMapping("/leitos_disponiveis")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listarLeitosDisponiveis() {
		List<LeitoEntity> lista = service.findLeitosDisponiveis();
		return new ResponseEntity<>(lista, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@Valid @RequestBody QuartoNovo model) throws Exception {
		ResultError errors = new ResultError();
		
		if (service.existeOutroQuartoComEsseNumero(model.getNumero())) {
			errors.addError("numero", "Existe outro Quarto com esse número");
		}
		
		if (!errors.getErrors().isEmpty()) {
			throw new ValidationException(errors);
		}
	
		QuartoEntity saved = null;
		saved = service.create(model);
	    return new ResponseEntity<>(saved, HttpStatus.OK);
	}
	
	@RequestMapping(value="/alterar", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarAlteracao(@Valid @RequestBody QuartoEdicao model) {
		ResultError errors = new ResultError();
		
		try {
			if (model.getId() != null) {
				if (service.existeOutroQuartoComEsseNumero(model.getId(), model.getNumero())) {
					errors.addError("numero", "Existe outro Quarto com esse número");
				}
			}
			
			if (!errors.getErrors().isEmpty()) {
				throw new ValidationException(errors);
			}
		
			QuartoEntity saved = null;
			saved = service.update(model);
		    return new ResponseEntity<>(saved, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value="/leito", method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravarLeito(@Valid @RequestBody LeitoEdicao model) {
		ResultError errors = new ResultError();
		
		try {
			if (model.getId() == null) {
				if (service.existeOutroLeitoComEsseNumero(model.getQuartoId(), model.getNumero())) {
					errors.addError("numero", "Existe outro Leito com esse número");
				}
			} else {
				if (service.existeOutroLeitoComEsseNumero(model.getId(), model.getQuartoId(), model.getNumero())) {
					errors.addError("numero", "Existe outro Leito com esse número");
				}
			}
			
			if (!errors.getErrors().isEmpty()) {
				throw new ValidationException(errors);
			}
		
			LeitoEntity saved = null;
			saved = service.saveLeito(model);
			return new ResponseEntity<>(saved, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<>(e.getRe(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
			return new ResponseEntity<>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	 }

	@RequestMapping(value="/leito/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluirLeito(@PathVariable("id") Long id) {
		try {
			service.removeLeito(id);
			return new ResponseEntity<>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping("/listas")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listas() {
		AutoWired retorno = new AutoWired();
		retorno.listaTipoLeito = tls.listSelect();
		retorno.listaDestinacaoHospedagem = dhs.listSelect();
		retorno.listaSituacaoLeito = sls.listSelect();
		retorno.listaTipoHospede = ths.listSelect();
		retorno.listaTipoServico = tss.listSelect();
		retorno.listaEntidade = etds.listSelect();
		return new ResponseEntity<>(retorno, HttpStatus.OK);
	}
	
	static class AutoWired {
		public List<ValorTexto> listaTipoLeito = new ArrayList<>();
		public List<ValorTexto> listaDestinacaoHospedagem = new ArrayList<>();
		public List<ValorTexto> listaSituacaoLeito = new ArrayList<>();
		public List<ValorTexto> listaTipoHospede = new ArrayList<>();
		public List<ValorTexto> listaTipoServico = new ArrayList<>();
		public List<ValorTexto> listaEntidade = new ArrayList<>();
	} 
	
}

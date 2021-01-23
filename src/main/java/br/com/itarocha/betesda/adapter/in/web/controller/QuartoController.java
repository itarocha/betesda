package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.dto.ApiError;
import br.com.itarocha.betesda.application.*;
import br.com.itarocha.betesda.application.port.in.*;
import br.com.itarocha.betesda.domain.*;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
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

	private final QuartoUseCase service;
	private final ValidatorUtil validationUtils;

	// TODO serviços devem sair daqui
	private final TipoLeitoUseCase tls;
	private final DestinacaoHospedagemUseCase dhs;
	private final SituacaoLeitoUseCase sls;
	private final TipoHospedeUseCase ths;
	private final TipoServicoUseCase tss;
	private final EntidadeService etds;


	@GetMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Quarto>> listar() {
		return ResponseEntity.ok(service.findAll());
	}

	@GetMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<Quarto> getById(@PathVariable("id") Long id) {
		Quarto model = service.find(id);
		if (model != null) {
			return ResponseEntity.ok(model);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<EditLeitoVO> getLeitoById(@PathVariable("id") Long id) {
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
			return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/por_destinacao_hospedagem/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Quarto>> listarByDestinacaoHospedagem(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findAllByDestinacaoHospedagem(id));
	}

	@GetMapping("/{id}/leitos")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Leito>> listarLeitosByQuarto(@PathVariable("id") Long id) {
		return ResponseEntity.ok(service.findLeitosByQuarto(id));
	}

	@GetMapping("/leitos_disponiveis")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<Leito>> listarLeitosDisponiveis() {
		return ResponseEntity.ok(service.findLeitosDisponiveis());
	}

	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<Quarto> gravar(@RequestBody QuartoNew model) throws Exception {
		validationUtils.validate(model);
		return ResponseEntity.ok(service.create(model));
	}
	
	@PostMapping("/alterar")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<Quarto> gravarAlteracao(@RequestBody QuartoEdit model) {
		validationUtils.validate(model);
		try {
			Quarto saved = service.update(model);
		    return ResponseEntity.ok(saved);
		} catch (Exception e) {
			return new ResponseEntity(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/leito")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<Leito> gravarLeito(@RequestBody EditLeitoVO model) {
		validationUtils.validate(model);
		try {
			return ResponseEntity.ok(service.saveLeito(model));
		} catch (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
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
			return new ResponseEntity<ApiError>(new ApiError(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }

	// TODO serviços devem sair daqui
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

package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.core.ports.in.*;
import br.com.itarocha.betesda.core.service.EntidadeService;
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
		Quarto saved = service.update(model);
		return ResponseEntity.ok(saved);
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
		service.remove(id);
		return ResponseEntity.noContent().build();
	 }

	@DeleteMapping("/leito/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluirLeito(@PathVariable("id") Long id) {
		service.removeLeito(id);
		return ResponseEntity.noContent().build();
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
		public List<ItemDictionary> listaTipoLeito = new ArrayList<>();
		public List<ItemDictionary> listaDestinacaoHospedagem = new ArrayList<ItemDictionary>();
		public List<ItemDictionary> listaSituacaoLeito = new ArrayList<>();
		public List<ItemDictionary> listaTipoHospede = new ArrayList<>();
		public List<ItemDictionary> listaTipoServico = new ArrayList<>();
		public List<ItemDictionary> listaEntidade = new ArrayList<>();
	} 
}

package br.com.itarocha.betesda.controller;

import br.com.itarocha.betesda.exception.ValidationException;
import br.com.itarocha.betesda.model.HospedagemFullVO;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.model.HospedeVO;
import br.com.itarocha.betesda.model.hospedagem.*;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;
import br.com.itarocha.betesda.service.HospedagemService;
import br.com.itarocha.betesda.service.PlanilhaGeralService;
import br.com.itarocha.betesda.service.RelatorioGeralService;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import br.com.itarocha.betesda.util.validation.ResultError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/app/hospedagem")
public class HospedagemController {

	@Autowired
	private HospedagemService service;
	
	@Autowired
	private RelatorioGeralService relatorioService;
	
	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody HospedagemVO model) {
		ItaValidator<HospedagemVO> v = new ItaValidator<HospedagemVO>(model);
		v.validate();
		
		if (model.getHospedes().size() == 0) {
			v.addError("id", "É necessário pelo menos um hóspede");
		} else {
			for (HospedeVO h : model.getHospedes()) {
				if ("T".equals(model.getTipoUtilizacao()) && (h.getAcomodacao() == null)) {
					v.addError("id", String.format("É necessário informar o Leito para o Hóspede [%s]", h.getPessoaNome()));
				}
				/*
				if (!service.pessoaLivre(h.getPessoaId())) {
					v.addError("id", String.format("[%s] está utilizando uma Hospedagem ainda pendente", h.getPessoaNome()));
				}
				*/
				if ("T".equals(model.getTipoUtilizacao()) && (h.getAcomodacao() != null) && 
					(h.getAcomodacao().getLeitoId() != null) && 
					(model.getDataEntrada() != null) && (model.getDataPrevistaSaida() != null) )
				{
					Long leitoId = h.getAcomodacao().getLeitoId();
					LocalDate dataIni = model.getDataEntrada();
					LocalDate dataFim = model.getDataPrevistaSaida();
					Integer leitoNumero = h.getAcomodacao().getLeitoNumero();
					Integer quartoNumero = h.getAcomodacao().getQuartoNumero();
					
					if (!service.leitoLivreNoPeriodo(leitoId, dataIni, dataFim)) {
						v.addError("id", String.format("Quarto %s Leito %s está ocupado no perído", quartoNumero, leitoNumero ));
					}
				}
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			service.create(model);
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/mapa")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaRetorno mapa(@RequestBody MapaHospedagemRequest model)
	{
		MapaRetorno retorno = service.buildMapaRetorno(model.data);
		return retorno;
	}

	@PostMapping("/mapa/linhas")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaLinhas mapaLinhas(@RequestBody MapaHospedagemRequest model)
	{
		MapaLinhas retorno = service.buildMapaLinhas(model.data);
		return retorno;
	}

	@PostMapping("/mapa/hospedes")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaHospedes mapaHospedes(@RequestBody MapaHospedagemRequest model)
	{
		MapaHospedes retorno = service.buildMapaHospedes(model.data);
		return retorno;
	}

	@PostMapping("/mapa/cidades")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaCidades mapaCidades(@RequestBody MapaHospedagemRequest model)
	{
		MapaCidades retorno = service.buildMapaCidades(model.data);
		return retorno;
	}

	@PostMapping("/mapa/quadro")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaQuadro mapaQuadro(@RequestBody MapaHospedagemRequest model)
	{
		MapaQuadro retorno = service.buildMapaQuadro(model.data);
		return retorno;
	}

	@PostMapping("/planilha_geral")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> planilhaGeral(@RequestBody PeriodoRequest model)
	{
		ItaValidator<PeriodoRequest> v = new ItaValidator<PeriodoRequest>(model);
		v.validate();
		
		if (model.dataIni == null) {
			v.addError("dataIni", "Data Inicial deve ser preenchida");
		}
		if (model.dataIni == null) {
			v.addError("dataFim", "Data Final deve ser preenchida");
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}

		//relatorioService.teste(model.dataIni,  model.dataFim);
		
		try {
			//RelatorioAtendimentos retorno = relatorioService.buildPlanilhaGeral(model.dataIni, model.dataFim);
			RelatorioAtendimentos retorno = relatorioService.buildNovaPlanilha(model.dataIni, model.dataFim);
			return new ResponseEntity<RelatorioAtendimentos>(retorno, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	//https://grokonez.com/spring-framework/spring-boot/excel-file-download-from-springboot-restapi-apache-poi-mysql
	//@GetMapping(value = "/planilha_geral_arquivo")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	@PostMapping("/planilha_geral_arquivo")
	public ResponseEntity<?>planilhaGeralExcel(@RequestBody PeriodoRequest model) throws IOException {
		ItaValidator<PeriodoRequest> v = new ItaValidator<PeriodoRequest>(model);
		v.validate();
		
		if (model.dataIni == null) {
			v.addError("dataIni", "Data Inicial deve ser preenchida");
		}
		if (model.dataIni == null) {
			v.addError("dataFim", "Data Final deve ser preenchida");
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		RelatorioAtendimentos retorno = null;
		try {
			//System.out.println(String.format("Iniciando geração do relatório - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
			//retorno = relatorioService.buildPlanilhaGeral(model.dataIni, model.dataFim);
			retorno = relatorioService.buildNovaPlanilha(model.dataIni, model.dataFim);
			//System.out.println(String.format("Finalizando geração do relatório - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
			// Gerar planilha
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		//System.out.println(String.format("Gerando planilha - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
		ByteArrayInputStream in = PlanilhaGeralService.toExcel(retorno);
		//System.out.println(String.format("Planilha gerada - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=planilha.xlsx");
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
		
		return ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				//.contentLength(file.length())
				.headers(headers)
				.body(new InputStreamResource(in));
	}
	
	@PostMapping("/leitos_ocupados")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> leitosOcupados(@RequestBody HospedagemPeriodoRequest model)
	{
		try {
			List<OcupacaoLeito> retorno = service.getLeitosOcupadosNoPeriodo(model.hospedagemId, model.dataIni, model.dataFim);
			
			//List<Long> retorno = service.getLeitosOcupadosNoPeriodo(model.hospedagemId, model.dataIni, model.dataFim);
			return new ResponseEntity<List<OcupacaoLeito>>(retorno, HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/mapa/alterar_hospede")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> alterarHospede(@RequestBody AlteracaoHospedeRequest model)
	{
		try {
			service.alterarTipoHospede(model.hospedeId, model.tipoHospedeId);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/mapa/encerramento")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> encerramento(@RequestBody OperacoesRequest model)
	{
		try {
			service.encerrarHospedagem(model.hospedagemId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/mapa/renovacao")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> renovacao(@RequestBody OperacoesRequest model)
	{
		try {
			service.renovarHospedagem(model.hospedagemId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK);
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/remover_hospede")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> removerHospede(@RequestBody RemoverHospedeRequest model)
	{
		try {
			service.removerHospede(model.hospedagemId, model.hospedeId);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value="/mapa/baixar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> baixar(@RequestBody BaixaRequest model)
	{
		try {
			service.baixarHospede(model.hospedeId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/mapa/transferir")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> transferir(@RequestBody TransferenciaRequest model)
	{
		try {
			service.transferirHospede(model.hospedeId, model.leitoId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/mapa/adicionar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> adicionarHospede(@RequestBody AdicionarHospedeRequest model)
	{
		try {
			service.adicionarHospede(model.hospedagemId, model.pessoaId, model.tipoHospedeId, model.leitoId, model.data);
			return new ResponseEntity<String>("ok", HttpStatus.OK); 
		} catch(ValidationException e) {
			return new ResponseEntity<ResultError>(e.getRe(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/mapa/hospedagem_info")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public HospedagemFullVO getHospedagemInfo(@RequestBody HospdeagemInfoRequest model)
	{
		HospedagemFullVO h = service.getHospedagemPorHospedeLeitoId(model.hospedagemId);
		return h;
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.excluirHospedagem(id);
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	private static class MapaHospedagemRequest{
		public LocalDate data;
	}

	private static class HospedagemPeriodoRequest{
		public Long hospedagemId;
		public LocalDate dataIni;
		public LocalDate dataFim;
	}
	
	private static class PeriodoRequest{
		public LocalDate dataIni;
		public LocalDate dataFim;
	}

	private static class BaixaRequest{
		public LocalDate data;
		public Long hospedeId;
	}
	
	private static class TransferenciaRequest{
		public LocalDate data;
		public Long hospedeId;
		public Long leitoId;
	}
	
	private static class AlteracaoHospedeRequest {
		public Long hospedeId;
		public Long tipoHospedeId;
	}
	
	private static class AdicionarHospedeRequest{
		public Long hospedagemId;
		public Long pessoaId;
		public Long tipoHospedeId;
		public LocalDate data;
		public Long leitoId;
	}

	private static class RemoverHospedeRequest{
		public Long hospedagemId;
		public Long hospedeId;
	}
	
	private static class OperacoesRequest{
		public LocalDate data;
		public Long hospedagemId;
	}

	private static class HospdeagemInfoRequest{
		public Long hospedagemId;
	}
}

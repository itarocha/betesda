package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.in.web.dto.*;
import br.com.itarocha.betesda.application.*;
import br.com.itarocha.betesda.application.port.in.HospedagemUseCase;
import br.com.itarocha.betesda.domain.hospedagem.*;
import br.com.itarocha.betesda.domain.HospedagemFullVO;
import br.com.itarocha.betesda.domain.HospedagemVO;
import br.com.itarocha.betesda.domain.HospedeVO;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;
import br.com.itarocha.betesda.util.validation.ItaValidator;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class HospedagemController {

	private final HospedagemUseCase hospedagemService;
	private final RelatorioGeralService relatorioService;
	private final MapaHospedagemService mapaHospedagemService;
	private final MapaHospedesService mapaHospedesService;
	private final MapaCidadesService mapaCidadesService;
	private final MapaQuadroService mapaQuadroService;

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
					
					if (!hospedagemService.leitoLivreNoPeriodo(leitoId, dataIni, dataFim)) {
						v.addError("id", String.format("Quarto %s Leito %s está ocupado no perído", quartoNumero, leitoNumero ));
					}
				}
			}
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getValidationResult(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			hospedagemService.create(model);
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/mapa")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaRetorno mapa(@RequestBody MapaHospedagemRequest model)
	{
		MapaRetorno retorno = mapaHospedagemService.buildMapaRetorno(model.getData());
		return retorno;
	}

	@PostMapping("/mapa/teste")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<List<HospedeMapa>> mapaTeste(@RequestBody PeriodoRequest model)
	{
		return ResponseEntity.ok(mapaHospedagemService.buildListaHospedeMapa(model.getDataIni(), model.getDataFim()));
	}

	/*
	@PostMapping("/mapa/linhas")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaLinhas mapaLinhas(@RequestBody MapaHospedagemRequest model)
	{
		MapaLinhas retorno = mapaHospedagemService.buildMapaLinhas(model.data);
		return retorno;
	}
	*/

	@PostMapping("/mapa/hospedes")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaHospedes mapaHospedes(@RequestBody MapaHospedagemRequest model)
	{
		MapaHospedes retorno = mapaHospedesService.buildMapaHospedes(model.getData());
		return retorno;
	}

	@PostMapping("/mapa/cidades")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaCidades mapaCidades(@RequestBody MapaHospedagemRequest model)
	{
		MapaCidades retorno = mapaCidadesService.buildMapaCidades(model.getData());
		return retorno;
	}

	@PostMapping("/mapa/quadro")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public MapaQuadro mapaQuadro(@RequestBody MapaHospedagemRequest model)
	{
		MapaQuadro retorno = mapaQuadroService.buildMapaQuadro(model.getData());
		return retorno;
	}

	@PostMapping("/planilha_geral")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> planilhaGeral(@RequestBody PeriodoRequest model)
	{
		ItaValidator<PeriodoRequest> v = new ItaValidator<PeriodoRequest>(model);
		v.validate();
		
		if (model.getDataIni() == null) {
			v.addError("dataIni", "Data Inicial deve ser preenchida");
		}
		if (model.getDataFim() == null) {
			v.addError("dataFim", "Data Final deve ser preenchida");
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getValidationResult(), HttpStatus.BAD_REQUEST);
		}

		//relatorioService.teste(model.dataIni,  model.dataFim);
		
		try {
			//RelatorioAtendimentos retorno = relatorioService.buildPlanilhaGeral(model.dataIni, model.dataFim);
			RelatorioAtendimentos retorno = relatorioService.buildNovaPlanilha(model.getDataIni(), model.getDataFim());
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
		
		if (model.getDataIni() == null) {
			v.addError("dataIni", "Data Inicial deve ser preenchida");
		}
		if (model.getDataFim() == null) {
			v.addError("dataFim", "Data Final deve ser preenchida");
		}
		
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getValidationResult(), HttpStatus.BAD_REQUEST);
		}
		
		RelatorioAtendimentos retorno = null;
		try {
			//System.out.println(String.format("Iniciando geração do relatório - %s", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)));
			//retorno = relatorioService.buildPlanilhaGeral(model.dataIni, model.dataFim);
			retorno = relatorioService.buildNovaPlanilha(model.getDataIni(), model.getDataFim());
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
			List<OcupacaoLeito> retorno = hospedagemService.getLeitosOcupadosNoPeriodo(model.getHospedagemId(), model.getDataIni(), model.getDataFim());
			
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
		hospedagemService.alterarTipoHospede(model.getHospedeId(), model.getTipoHospedeId());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping("/mapa/encerramento")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> encerramento(@RequestBody OperacoesRequest model)
	{
		hospedagemService.encerrarHospedagem(model.hospedagemId, model.data);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping("/mapa/renovacao")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> renovacao(@RequestBody OperacoesRequest model)
	{
		hospedagemService.renovarHospedagem(model.hospedagemId, model.data);
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping("/remover_hospede")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> removerHospede(@RequestBody RemoverHospedeRequest model)
	{
		hospedagemService.removerHospede(model.getHospedagemId(), model.getHospedeId());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping(value="/mapa/baixar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> baixar(@RequestBody BaixaRequest model)
	{
		hospedagemService.baixarHospede(model.getHospedeId(), model.getData());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping("/mapa/transferir")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> transferir(@RequestBody TransferenciaRequest model)
	{
		hospedagemService.transferirHospede(model.getHospedeId(), model.getLeitoId(), model.getData());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping("/mapa/adicionar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> adicionarHospede(@RequestBody AdicionarHospedeRequest model)
	{
		hospedagemService.adicionarHospede(	model.getHospedagemId(),
											model.getPessoaId(),
											model.getTipoHospedeId(),
											model.getLeitoId(),
											model.getData());
		return new ResponseEntity<String>("ok", HttpStatus.OK);
	}
	
	@PostMapping("/mapa/hospedagem_info")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public HospedagemFullVO getHospedagemInfo(@RequestBody HospdeagemInfoRequest model)
	{
		HospedagemFullVO h = hospedagemService.getHospedagemPorHospedeLeitoId(model.getHospedagemId());
		return h;
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			hospedagemService.excluirHospedagem(id);
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}

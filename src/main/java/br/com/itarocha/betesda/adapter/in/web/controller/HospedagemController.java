package br.com.itarocha.betesda.adapter.in.web.controller;

import br.com.itarocha.betesda.adapter.in.web.dto.*;
import br.com.itarocha.betesda.application.*;
import br.com.itarocha.betesda.application.port.in.HospedagemUseCase;
import br.com.itarocha.betesda.domain.hospedagem.*;
import br.com.itarocha.betesda.domain.Hospedagem;
import br.com.itarocha.betesda.domain.HospedagemNew;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;
import br.com.itarocha.betesda.util.validacoes.ValidatorUtil;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	private final ValidatorUtil validationUtils;

	@PostMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<Hospedagem> gravar(@RequestBody HospedagemNew model) {
		validationUtils.validate(model);

		try {
		    return ResponseEntity.ok(hospedagemService.create(model));
		} catch (Exception e) {
			return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
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

	@PostMapping("/mapa/hospedes")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<MapaHospedes> mapaHospedes(@RequestBody MapaHospedagemRequest model)
	{
		return ResponseEntity.ok(mapaHospedesService.buildMapaHospedes(model.getData()));
	}

	@PostMapping("/mapa/cidades")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<MapaCidades> mapaCidades(@RequestBody MapaHospedagemRequest model)
	{
		return ResponseEntity.ok(mapaCidadesService.buildMapaCidades(model.getData()));
	}

	@PostMapping("/mapa/quadro")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<MapaQuadro> mapaQuadro(@RequestBody MapaHospedagemRequest model)
	{
		return ResponseEntity.ok(mapaQuadroService.buildMapaQuadro(model.getData()));
	}

	@PostMapping("/planilha_geral")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<RelatorioAtendimentos> planilhaGeral(@RequestBody PeriodoRequest model)
	{
		validationUtils.validate(model);
		return ResponseEntity.ok(relatorioService.buildNovaPlanilha(model.getDataIni(), model.getDataFim()));
	}
	
	//https://grokonez.com/spring-framework/spring-boot/excel-file-download-from-springboot-restapi-apache-poi-mysql
	//@GetMapping(value = "/planilha_geral_arquivo")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	@PostMapping("/planilha_geral_arquivo")
	public ResponseEntity<?>planilhaGeralExcel(@RequestBody PeriodoRequest model) throws IOException {
		validationUtils.validate(model);
		
		RelatorioAtendimentos retorno = null;
		try {
			System.out.println(String.format("%s - Gerando dados", LocalDateTime.now()));
			retorno = relatorioService.buildNovaPlanilha(model.getDataIni(), model.getDataFim());
		} catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		System.out.println(String.format("%s - Gerando planilha", LocalDateTime.now()));
		ByteArrayInputStream in = PlanilhaGeralService.toExcel(retorno);
		System.out.println(String.format("%s - Planilha gerada", LocalDateTime.now()));
		
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
	public ResponseEntity<List<OcupacaoLeito>> leitosOcupados(@RequestBody HospedagemPeriodoRequest model)
	{
		return ResponseEntity.ok(hospedagemService.getLeitosOcupadosNoPeriodo(model.getHospedagemId(), model.getDataIni(), model.getDataFim()));
	}

	@PostMapping("/mapa/alterar_hospede")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity alterarHospede(@RequestBody AlteracaoHospedeRequest model)
	{
		hospedagemService.alterarTipoHospede(model.getHospedeId(), model.getTipoHospedeId());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/mapa/encerramento")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity encerramento(@RequestBody OperacoesRequest model)
	{
		hospedagemService.encerrarHospedagem(model.hospedagemId, model.data);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/mapa/renovacao")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity renovacao(@RequestBody OperacoesRequest model)
	{
		hospedagemService.renovarHospedagem(model.hospedagemId, model.data);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/remover_hospede")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity removerHospede(@RequestBody RemoverHospedeRequest model)
	{
		hospedagemService.removerHospede(model.getHospedagemId(), model.getHospedeId());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(value="/mapa/baixar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity baixar(@RequestBody BaixaRequest model)
	{
		hospedagemService.baixarHospede(model.getHospedeId(), model.getData());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/mapa/transferir")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity transferir(@RequestBody TransferenciaRequest model)
	{
		hospedagemService.transferirHospede(model.getHospedeId(), model.getLeitoId(), model.getData());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/mapa/adicionar")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity adicionarHospede(@RequestBody AdicionarHospedeRequest model)
	{
		hospedagemService.adicionarHospede(	model.getHospedagemId(),
											model.getPessoaId(),
											model.getTipoHospedeId(),
											model.getLeitoId(),
											model.getData());
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/mapa/hospedagem_info")
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<Hospedagem> getHospedagemInfo(@RequestBody HospdeagemInfoRequest model)
	{
		return ResponseEntity.ok(hospedagemService.getHospedagemPorHospedeLeitoId(model.getHospedagemId()));
	}

	@DeleteMapping("{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity excluir(@PathVariable("id") Long id) {
		hospedagemService.excluirHospedagem(id);
		return ResponseEntity.noContent().build();
	}
}
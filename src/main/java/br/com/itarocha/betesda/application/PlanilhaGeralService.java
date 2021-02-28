package br.com.itarocha.betesda.application;

import br.com.itarocha.betesda.report.ChaveValor;
import br.com.itarocha.betesda.report.RelatorioAtendimentos;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class PlanilhaGeralService {

	public static ByteArrayInputStream toExcel(RelatorioAtendimentos dados) throws IOException {

		String[] colsPlanilhaGeral = {	"Atendimento", 
										"Id Pessoa", 
										"Nome", 
										"Nascimento", 
										"Idade", 
										"Faixa Etária", 
										"Tipo", 
										"Encaminhador", 
										"Cidade de Origem", 
										"UF Origem",
										"Tipo de Hospedagem", 
										"Dt. Ingresso", 
										"Dt. Desligamento", 
										"Dias"};
		
		String[] colsAtividades = { "Descrição", "Quantidade"};
		
		try(
				Workbook workbook = new XSSFWorkbook();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
			)
		{
			CreationHelper createHelper = workbook.getCreationHelper();

			// Estilos
			CellStyle ageCellStyle = workbook.createCellStyle();
			ageCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("0"));
			
			CellStyle dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy"));

			CellStyle diaMesCellStyle = workbook.createCellStyle();
			diaMesCellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("dd/mm"));
			
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setColor(IndexedColors.GREEN.getIndex());

			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
			headerCellStyle.setFont(headerFont);
			
			Font fontNegrito = workbook.createFont();
			fontNegrito.setBold(true);
			fontNegrito.setColor(IndexedColors.BLACK.getIndex());

			CellStyle headerNegritoStyle = workbook.createCellStyle();
			headerNegritoStyle.setFont(fontNegrito);

			// Planilha Geral
			Sheet sheetPlanilhaGeral = workbook.createSheet("Planilha Geral");

			Row headerRow = sheetPlanilhaGeral.createRow(0);

			// Header
			for (int col = 0; col < colsPlanilhaGeral.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(colsPlanilhaGeral[col]);
				cell.setCellStyle(headerCellStyle);
			}

			int[] rowIdx = {1};
			dados.getResumoHospedagens().forEach(h -> {
				Row row = sheetPlanilhaGeral.createRow(rowIdx[0]++);

				row.createCell(0).setCellValue(h.getHospedagemId());
				row.createCell(1).setCellValue(h.getPessoa().getId());
				row.createCell(2).setCellValue(h.getPessoa().getNome());

				Cell dateCell = row.createCell(3);
				Date date = Date.from(h.getPessoa().getDataNascimento().atStartOfDay(ZoneId.systemDefault()).toInstant());
				dateCell.setCellValue(date);
				dateCell.setCellStyle(dateCellStyle);
				
				Cell ageCell = row.createCell(4);
				ageCell.setCellValue(h.getPessoa().getIdade());
				ageCell.setCellStyle(ageCellStyle);
				
				row.createCell(5).setCellValue(h.getPessoa().getFaixaEtaria());
				String tipoUtilizacao = "T".equals(h.getTipoUtilizacao())? "Total" : "Parcial";
				row.createCell(6).setCellValue(tipoUtilizacao);
				row.createCell(7).setCellValue(h.getEntidadeNome());
				row.createCell(8).setCellValue(h.getPessoa().getCidade());
				row.createCell(9).setCellValue(h.getPessoa().getUf());
				row.createCell(10).setCellValue(h.getTipoHospedeDescricao());

				Date dataEntrada = Date.from(h.getDataIni().atStartOfDay(ZoneId.systemDefault()).toInstant());
				row.createCell(11).setCellValue(dataEntrada);
				row.getCell(11).setCellStyle(diaMesCellStyle);
				
				Date dataSaida = Date.from(h.getDataFim().atStartOfDay(ZoneId.systemDefault()).toInstant());
				row.createCell(12).setCellValue(dataSaida);
				row.getCell(12).setCellStyle(diaMesCellStyle);

				row.createCell(13).setCellValue(h.getDias());
				row.getCell(13).setCellStyle(ageCellStyle);
			});
			
			for (int col = 0; col < colsPlanilhaGeral.length; col++) {
				sheetPlanilhaGeral.autoSizeColumn(col);
			}

			dados.getPlanilhas().forEach(planilha -> {
				// Ranking de Cidades
				Sheet sheet = workbook.createSheet(planilha.getTitulo());

				Row rowHeader = sheet.createRow(0);

				
				Cell cellK = rowHeader.createCell(0);
				cellK.setCellValue(planilha.getLabelChave());
				cellK.setCellStyle(headerCellStyle);
				
				Cell cellV = rowHeader.createCell(1);
				cellV.setCellValue("Quantidade");
				cellV.setCellStyle(headerCellStyle);
				
				int[] rowIdxCidades = {1};
				planilha.getLista().forEach(item -> {
					Row row = sheet.createRow(rowIdxCidades[0]++);
					row.createCell(0).setCellValue(item.getNome() );
					row.createCell(1).setCellValue(item.getQuantidade() );
					
				});

				for (int col = 0; col <= 1; col++) {
					sheet.autoSizeColumn(col);
				}
			});
			
			// Relatório de Atividades/Hospedagens
			Sheet sheetAtividades = workbook.createSheet("Atividades e Hospedagens");
			
			Row hrAtividades = sheetAtividades.createRow(0);

			// Header
			for (int col = 0; col < colsAtividades.length; col++) {
				Cell cell = hrAtividades.createCell(col);
				cell.setCellValue(colsAtividades[col]);
				cell.setCellStyle(headerNegritoStyle);
			}
			
			int[] rowAtividades = {1};
			
			dados.getAtividadesHospedagem().forEach(a -> {
				
				String titulo = a.getTitulo();
				
				List<ChaveValor> lista = a.getLista();
				
				// Cabeçalho
				Row rSubTit = sheetAtividades.createRow(rowAtividades[0]++);
				Cell cell = rSubTit.createCell(0);  
				cell.setCellStyle(headerNegritoStyle);
				cell.getCellStyle().setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());
				cell.setCellValue(titulo);
				
				lista.forEach(item -> {
					Row row = sheetAtividades.createRow(rowAtividades[0]++);
					row.createCell(0).setCellValue(item.getNome() );
					row.createCell(1).setCellValue(item.getQuantidade());
				});
				Row rEnd = sheetAtividades.createRow(rowAtividades[0]++);
				
			});
			for (int col = 0; col < colsAtividades.length; col++) {
				sheetAtividades.autoSizeColumn(col);
			}

			// Finalmente...
			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
		}
	}
}


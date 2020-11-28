package br.com.itarocha.betesda.testes;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PlanilhaPacienteBuilder {
    
	private static final String FILE_NAME =  "/home/itamar/projetos_spring/betesda/planilha/planilha_paciente.xlsx";

    public static void make(List<PlanilhaPaciente> lista) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Paciente");
        Object[][] datatypes = {
                {"Datatype", "Type", "Size(in bytes)"},
                {"int", "Primitive", 2},
                {"float", "Primitive", 4},
                {"double", "Primitive", 8},
                {"char", "Primitive", 1},
                {"String", "Non-Primitive", "No fixed size"}
        };

        int rowNum = 0;
        System.out.println("Creating excel");

        for (PlanilhaPaciente p : lista) {
        	Row row = sheet.createRow(rowNum++);
        	
        	int colNum = 0;
        	// Registro
        	Cell cell = row.createCell(colNum++);
        	cell.setCellValue(p.getCodigo());

        	// Nome
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getNome() + " " + p.getSobrenome());
        	
        	// CPF
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getCpf());
        	
        	cell = row.createCell(colNum++);
        	//cell.setCellType(CellType.NUMERIC);
        	cell.setCellValue( p.getDataNascimento() );

        	// RG
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getRg());

        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getNaturalidade());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getNacionalidade());

        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getEstadoCivil());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getProfissao());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getTelefone());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getTelefone2());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getEndereco());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getNumero());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getBairro());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue((String) p.getUf());
        	
        	cell = row.createCell(colNum++);
        	cell.setCellValue(p.getDataCadastro());
        }
        
        for (Object[] datatype : datatypes) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Done");
    }
}

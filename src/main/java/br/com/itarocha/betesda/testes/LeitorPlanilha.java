package br.com.itarocha.betesda.testes;

import br.com.itarocha.betesda.utils.ValidadorCpf;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
import br.itarocha.hotelaria.model.PlanilhaEstadia;
import br.itarocha.hotelaria.model.PlanilhaPaciente;
import br.itarocha.hotelaria.repository.PlanilhaEstadiaRepository;
import br.itarocha.hotelaria.repository.PlanilhaPacienteRepository;
*/

@Component
public class LeitorPlanilha {
	
	private static final String FILE_NAME = "/home/itamar/projetos_spring/betesda/planilha/betesda122018.xlsm";
	
	//@Autowired
	//private PlanilhaPacienteRepository repoPaciente;
	
	//@Autowired
	//private PlanilhaEstadiaRepository repoEstadia;
	
	public static void main(String... args) {
		
		
		LeitorPlanilha lp = new LeitorPlanilha();
		//lp.buildHospedes();
		//repoEstadia.deleteAll();
		//lp.buildEstadias("Estadia");
		//lp.buildEstadias("Estadias_Antigas");

		// 0 - Cidades por estados 
		// 1 - Estados 
		
		lp.go(TipoPlanilha.PACIENTE); 
		//lp.go("Estadia"); 
	}

	public LeitorPlanilha() {
	}
	
	private void go(TipoPlanilha tipo) {
		List<PlanilhaPaciente> lista = new ArrayList<>();
		
        try {
            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            String planilha = "Paciente";
            switch (tipo) {
	        	case PACIENTE : {
	        		planilha = "Paciente";
	        		break;
	        	}
	        	case ESTADIA : {
	        		planilha = "Estadia";
	        		break;
	        	}
	        	default : planilha = "Paciente";
            }
            
            Sheet datatypeSheet = workbook.getSheet(planilha);
            int i = 0;
            for (Row row: datatypeSheet) {
            	if (TipoPlanilha.PACIENTE.equals(tipo)) {
            		i++;
            		PlanilhaPaciente p = extractPlanilhaPaciente(row);
            		lista.add(p);
            		System.out.println(String.format("%d - %s %s", i, p.getNome(), p.getSobrenome() ) );
            	} else if (TipoPlanilha.ESTADIA.equals(tipo)) {
                		
                }
            	/*
                System.out.print("#"+row.getRowNum()+"--");
                for (Cell cell : row) {	
                	System.out.print( tratarConteudoCelula(cell) );
                }
                System.out.println();
                */
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }	
        System.out.println("PRONTO!!! ");
        
        PlanilhaPacienteBuilder.make(lista);
	}
	
	private PlanilhaPaciente extractPlanilhaPaciente(Row row) {
		DataFormatter formatter = new DataFormatter();

		PlanilhaPaciente p = new PlanilhaPaciente();
		//System.out.println("LINHA #" + row.getRowNum());
		
		p.setCodigo( formatter.formatCellValue(row.getCell(0)) );
		
		String cpf = validarCpf(formatter.formatCellValue(row.getCell(1)));
		cpf = ValidadorCpf.isCPF(cpf) ? cpf : "";
		
		p.setCpf(cpf );
		p.setRg(tratarTexto(formatter.formatCellValue(row.getCell(2)).trim().replaceAll(" ", "")) );
		p.setNome(tratarTexto( formatter.formatCellValue(row.getCell(3)) ));
		p.setSobrenome(tratarTexto( formatter.formatCellValue(row.getCell(4)) ));
		try {
			p.setDataNascimento(parseDate(row.getCell(5),false));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Nascimento. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		p.setNaturalidade(tratarTexto( formatter.formatCellValue(row.getCell(6)) ));
		p.setNacionalidade(tratarTexto( formatter.formatCellValue(row.getCell(7)) ));
		p.setEstadoCivil(tratarTexto( formatter.formatCellValue(row.getCell(8)) ));
		p.setProfissao(tratarTexto( formatter.formatCellValue(row.getCell(9)) ));
		p.setTelefone(formatter.formatCellValue(row.getCell(10)).trim());
		p.setTelefone2(formatter.formatCellValue(row.getCell(11)).trim());
		p.setEndereco(tratarTexto(formatter.formatCellValue(row.getCell(12))) );
		p.setNumero(formatter.formatCellValue(row.getCell(13)));
		p.setBairro(tratarTexto(  formatter.formatCellValue(row.getCell(14)) ));
		p.setCidade(tratarTexto( formatter.formatCellValue(row.getCell(15)) ));
		p.setUf(removerAcentos( formatter.formatCellValue(row.getCell(16)) ));
		p.setCep(formatter.formatCellValue(row.getCell(17)).trim() );

		try {
			p.setDataCadastro(parseDate(row.getCell(18),true));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Cadastro. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		
		
		//repoPaciente.save(p);
		
		//p.setDataCadastro(row.getCell(18).getDateCellValue());
		return p;
		
	}
	
	
	
	//@PostConstruct
	public void buildEstadias() {
		//repoEstadia.deleteAll();
		//buildEstadias("Estadias_Antigas");
		//buildEstadias("Estadia");
	}
	
	/*
	public void buildEstadias(String folha) {
		

		System.out.println("ABRINDO PLANILHA....");
		
		//String fileName = "D:\\projetos\\betesdaCSharp\\BetesdaPlanilha\\CB.xlsm";
		String fileName = "/home/itamar/betesda/cb2.xlsm";
		int qtd = 0;
		try {
			// Sheet sheet1 = wb.getSheetAt(0);
			Workbook workbook;
			workbook = new XSSFWorkbook(OPCPackage.open(fileName));

			System.out.println("COMECANDO....");
			DataFormatter formatter = new DataFormatter();
			Sheet sheet1 = workbook.getSheet(folha); // getSheetAt(1); //
			for (Row row : sheet1) {
				if (row.getRowNum() == 0) {
					continue;
				}

				Cell cellRegistro = row.getCell(0);
				System.out.println("Registro: "+formatter.formatCellValue(cellRegistro));
				
				if (cellRegistro == null) {
					continue;
				}
				
				if (CellType.BLANK.equals(cellRegistro.getCellTypeEnum()) ) {
					continue;
				}
				
				qtd++;
				
				System.out.println("-----------------------------------------------------------");
				System.out.println("LINHA #" + (row.getRowNum()+1) );
				//inserirPlanilhaEstadia(row);
			}
			
			System.out.println("\n\n\nxlsm lido com sucesso!");
			System.out.println("QUANTIDADE DE LINHAS: "+qtd);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	///////@PostConstruct
	/*
	public void buildHospedes() {
		repoPaciente.deleteAll();

		
		//String fileName = "D:\\projetos\\betesdaCSharp\\BetesdaPlanilha\\CB.xlsm";
		String fileName = "/home/itamar/projetos_spring/betesdacsharp/betesdaplanilha/cb2.xlsm";
		
		try {
			// Sheet sheet1 = wb.getSheetAt(0);
			Workbook workbook;
			workbook = new XSSFWorkbook(OPCPackage.open(fileName));

			System.out.println("COMECANDO....");
			DataFormatter formatter = new DataFormatter();
			Sheet sheet1 = workbook.getSheet("Paciente"); // getSheetAt(1); //
			//Sheet sheet1 = workbook.getSheet("Estadia"); // getSheetAt(1); //
			for (Row row : sheet1) {
				if (row.getRowNum() == 0) {
					continue;
				}
				
				

				System.out.println("-----------------------------------------------------------");
				System.out.println("LINHA #" + (row.getRowNum()+1) );
				Cell cellTelefone2 = row.getCell(11);
				//System.out.println("Telefone2: "+formatter.formatCellValue(cellTelefone2));
				//System.out.println("-----------------------------------------------------------");
				for (Cell cell : row) {
					
					System.out.println(String.format("columnIndex() = %s", cell.getColumnIndex()));
					
					CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
					System.out.print(cellRef.formatAsString());
					System.out.print(" - ");

					// get the text that appears in the cell by getting the cell value and applying
					// any data formats (Date, 0.00, 1.23e9, $1.23, etc)
					String text = formatter.formatCellValue(cell);

					String conteudo = tratarConteudoCelula(cell);

					System.out.println(String.format("%s - %s",conteudo, text));
					
				}
				inserirPlanilhaPaciente(row);
			}

			// DO STUF WITH WORKBOOK

			// FileOutputStream out = new FileOutputStream(new File(fileName));
			// workbook.write(out);
			// out.close();
			System.out.println("xlsm lido com sucesso!");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	private void inserirPlanilhaPaciente(Row row) {
		DataFormatter formatter = new DataFormatter();

		PlanilhaPaciente p = new PlanilhaPaciente();
		//System.out.println("LINHA #" + row.getRowNum());
		
		p.setCodigo( formatter.formatCellValue(row.getCell(0)) );
		p.setCpf(formatter.formatCellValue(row.getCell(1)).trim().replaceAll(" ", "") );
		p.setRg(tratarTexto(formatter.formatCellValue(row.getCell(2)).trim().replaceAll(" ", "")) );
		p.setNome(tratarTexto( formatter.formatCellValue(row.getCell(3)) ));
		p.setSobrenome(tratarTexto( formatter.formatCellValue(row.getCell(4)) ));
		try {
			p.setDataNascimento(parseDate(row.getCell(5),false));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Nascimento. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		p.setNaturalidade(tratarTexto( formatter.formatCellValue(row.getCell(6)) ));
		p.setNacionalidade(tratarTexto( formatter.formatCellValue(row.getCell(7)) ));
		p.setEstadoCivil(tratarTexto( formatter.formatCellValue(row.getCell(8)) ));
		p.setProfissao(tratarTexto( formatter.formatCellValue(row.getCell(9)) ));
		p.setTelefone(formatter.formatCellValue(row.getCell(10)).trim());
		p.setTelefone2(formatter.formatCellValue(row.getCell(11)).trim());
		p.setEndereco(tratarTexto(formatter.formatCellValue(row.getCell(12))) );
		p.setNumero(formatter.formatCellValue(row.getCell(13)));
		p.setBairro(tratarTexto(  formatter.formatCellValue(row.getCell(14)) ));
		p.setCidade(tratarTexto( formatter.formatCellValue(row.getCell(15)) ));
		p.setUf(removerAcentos( formatter.formatCellValue(row.getCell(16)) ));
		p.setCep(formatter.formatCellValue(row.getCell(17)).trim() );

		try {
			p.setDataCadastro(parseDate(row.getCell(18),true));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Cadastro. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		
		
		repoPaciente.save(p);
		
		//p.setDataCadastro(row.getCell(18).getDateCellValue());
		
	}
	*/
	
	/*
	private void inserirPlanilhaEstadia(Row row) {
		DataFormatter formatter = new DataFormatter();

		PlanilhaEstadia p = new PlanilhaEstadia();
		//System.out.println("LINHA #" + row.getRowNum());
		
		p.setCodigo( formatter.formatCellValue(row.getCell(0)) );
		p.setCpf(formatter.formatCellValue(row.getCell(1)).trim().replaceAll(" ", "") );
		try {
			p.setDataEntrada(parseDate(row.getCell(11),false));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Entrada. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		
		try {
			p.setDataSaida(parseDate(row.getCell(12),true));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Saída. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		
		try {
			p.setDataExpectativaSaida(parseDate(row.getCell(13),true));
		} catch (Exception e) {
			System.out.println("ERRO ao importar Data de Expectativa de Saída. Linha " + (row.getRowNum()+1)+ " - "+e.getMessage());
		}
		p.setObservacoes(formatter.formatCellValue(row.getCell(15)).trim().replaceAll(" ", "") );
		
		repoEstadia.save(p);
		
	}
	*/
	
	private String tratarTexto(String texto) {
		texto = texto.trim();
		texto = removerAcentos(texto);
		return texto.toUpperCase();
		
	}
	
	private String validarCpf(String texto) {
		texto = texto.trim().replaceAll("\\D", "");
		// Completar com zeros até 11 caracteres
		
		if (texto.length() < 11) {
			int tam = (11 - texto.length());
			for (int i = 1; i <= tam; i++) {
				texto = "0" + texto;
			}
		}
		return texto;
	}

	private Date parseDate(Cell cell, boolean aceitaNulo) throws Exception{
		
		if (cell != null) {
			if (cell.getCellType().equals(CellType.NUMERIC)) {
				return cell.getDateCellValue();
			} else {
				try {
					DateFormat df = new SimpleDateFormat("dd/MM/yy");
					
					String texto = cell.getStringCellValue().trim();
					if (!(aceitaNulo && "".equals(texto))) {
						return df.parse(texto);
					}
				} catch (ParseException e) {
					throw new Exception(String.format("Conteúdo: [%s]. Mensagem: [%s]",cell.getStringCellValue().trim(),e.getMessage()));
				}
			}
		}
		return null;
	}
	

	private String tratarConteudoCelula(Cell cell) {
		String retorno = "";
		String rowCol = String.format(" CELL[%d]", cell.getColumnIndex());
		
		switch (cell.getCellType()) {
		case STRING:
			retorno = String.format(   "STRING.: [%s]", cell.getRichStringCellValue().getString());
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				retorno = String.format("DATE...: [%s]", cell.getDateCellValue());
			} else {
				retorno = String.format("NUMERIC: [%s]", cell.getNumericCellValue());
			}
			break;
		case BOOLEAN:
			retorno = String.format(    "BOOLEAN: [%s]", cell.getBooleanCellValue());
			break;
		case FORMULA:
			retorno = String.format(    "FORMULA: [%s]", cell.getCellFormula());
			break;
		case BLANK:
			retorno = "<EMPTY CELL>";
			break;
		default:
			retorno = "<UNKNOW CELL>";
		}
		return rowCol + retorno;
	}

	
	private static String removerAcentos(String acentuada) {
		CharSequence cs = new StringBuilder(acentuada);
		return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	
	private enum TipoPlanilha {
		PACIENTE,
		ESTADIA
	}
}
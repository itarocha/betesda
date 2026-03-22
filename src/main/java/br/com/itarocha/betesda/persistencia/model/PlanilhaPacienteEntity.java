package br.com.itarocha.betesda.persistencia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import java.util.Date;

@Entity(name="planilha_paciente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanilhaPacienteEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(length=16)
	private String codigo;

	@Column(length=16)
	private String cpf;
	
	@Column(length=32)
	private String rg;
	
	@Column(length=64)
	private String nome;

	@Column(length=64)
	private String sobrenome;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dataNascimento;
	
	@Column(length=64)
	private String naturalidade;
	
	@Column(length=64)
	private String nacionalidade;
	
	@Column(length=32)
	private String estadoCivil;

	@Column(length=64)
	private String profissao;

	@Column(length=32)
	private String telefone;

	@Column(length=32)
	private String telefone2;

	@Column(length=64)
	private String endereco;
	
	@Column(length=16)
	private String numero;

	@Column(length=64)
	private String bairro;

	@Column(length=64)
	private String cidade;
	
	@Column(length=64)
	private String uf;
	
	@Column(length=16)
	private String cep;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date dataCadastro;

	public void setCep(String cep) {
		String xcep = cep; 
		if (cep.length() > 16 ) {
			xcep = cep.substring(0,15);
		}
		this.cep = xcep;
	}
}

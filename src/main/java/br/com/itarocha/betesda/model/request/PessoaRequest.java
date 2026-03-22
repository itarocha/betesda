package br.com.itarocha.betesda.model.request;

import br.com.itarocha.betesda.model.EstadoCivil;
import br.com.itarocha.betesda.model.Sexo;
import br.com.itarocha.betesda.model.UnidadeFederacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PessoaRequest {

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;

	@NotNull(message="Data de Nascimento é obrigatório")
	private LocalDate dataNascimento;

	private Sexo sexo;
	
	private EstadoCivil estadoCivil;
	
	@Size(min = 0, max = 11, message="CPF deve ter 11 caracteres")
	private String cpf;
	
	@Size(min = 15, max = 15, message="Cartão do SUS deve ter 15 caracteres")
	private String cartaoSus;

	@Size(max = 32, message="RG deve ter até 32 caracteres")
	private String rg;
	
	@Size(max = 64, message="Naturalidade não pode ter mais que 64 caracteres")
	private String naturalidadeCidade;
	
	private UnidadeFederacao naturalidadeUf;
	
	@Size(max = 64, message="Nacionalidade não pode ter mais que 64 caracteres")
	private String nacionalidade;
	
	@Size(max = 64, message="Profissão não pode ter mais que 64 caracteres")
	private String profissao;

	@Valid
	@NotNull(message="Endereço deve ser preenchido")
	private EnderecoRequest endereco;
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Size(max = 16, message="Telefone 2 não pode ter mais que 16 caracteres")
	private String telefone2;

	@Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	private String observacoes;
}

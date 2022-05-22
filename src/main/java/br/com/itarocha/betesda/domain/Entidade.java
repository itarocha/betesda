package br.com.itarocha.betesda.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Entidade implements Serializable{
	
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 3 a 64 caracteres")
	private String nome;

	@NotNull(message="CNPJ é obrigatório")
	@Size(min = 14, max = 14, message="CNPJ deve ter 14 caracteres")
	private String cnpj;

	@Builder.Default
	@Valid
	@NotNull(message="Endereço é obrigatório")
	private Endereco endereco = new Endereco();
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;

	@Size(max = 16, message="Telefone2 não pode ter mais que 16 caracteres")
	private String telefone2;
	
	@Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	private String observacoes;

	@Builder.Default
	private List<Encaminhador> encaminhadores = new ArrayList<>();
}
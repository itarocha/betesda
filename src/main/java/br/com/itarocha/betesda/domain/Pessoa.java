package br.com.itarocha.betesda.domain;

import br.com.itarocha.betesda.domain.enums.EstadoCivilEnum;
import br.com.itarocha.betesda.domain.enums.SexoEnum;
import br.com.itarocha.betesda.domain.enums.UnidadeFederacaoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pessoa {
	
	private Long id;

	@NotNull(message="Nome é obrigatório")
	@Size(min = 3, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;

	@NotNull(message="Data de Nascimento é obrigatório")
	private LocalDate dataNascimento;

	private SexoEnum sexo = SexoEnum.F;
	
	private EstadoCivilEnum estadoCivil;
	
	@Size(min = 0, max = 11, message="CPF deve ter 11 caracteres")
	private String cpf;
	
	@Size(min = 15, max = 15, message="Cartão do SUS deve ter 15 caracteres")
	private String cartaoSus;

	@Size(max = 32, message="RG deve ter até 32 caracteres")
	private String rg;
	
	@Size(min = 3, max=64, message = "Naturalidade deve ter entre 3 a 64 caracteres")
	private String naturalidadeCidade;
	
	private UnidadeFederacaoEnum naturalidadeUf;

	@Size(min = 3, max=64, message = "Nacionalidade deve ter entre 3 a 64 caracteres")
	private String nacionalidade;

	@Size(min = 3, max=64, message = "Profissão deve ter entre 3 a 64 caracteres")
	private String profissao;

	@NotNull(message="Endereço deve ser preenchido")
	private Endereco endereco = new Endereco();
	
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Size(max = 16, message="Telefone 2 não pode ter mais que 16 caracteres")
	private String telefone2;

	@javax.validation.constraints.Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	private String observacoes;

	@Override
    public String toString() {
        return String.format("Pessoa[id=%d, nome='%s']",id, nome);
    }	
}

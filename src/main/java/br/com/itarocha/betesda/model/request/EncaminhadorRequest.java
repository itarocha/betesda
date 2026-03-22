package br.com.itarocha.betesda.model.request;

import br.com.itarocha.betesda.model.Logico;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EncaminhadorRequest {
	
	@NotNull(message="Entidade é obrigatória")
	private Long entidadeId;
	
	@NotNull(message="Nome é obrigatório")
	@Size(min = 2, max = 64, message="Nome deve ter entre 2 a 64 caracteres")
	private String nome;
	
	@NotNull(message="Cargo é obrigatório")
	@Size(min = 2, max = 64, message="Cargo deve ter entre 2 a 64 caracteres")
	private String cargo;

	@NotNull(message="Telefone é obrigatório")
	@Size(max = 16, message="Telefone não pode ter mais que 16 caracteres")
	private String telefone;
	
	@Email(message="Email inválido")
	@Size(max = 64, message="Email deve ter no máximo 64 caracteres")
	private String email;
	
	@NotNull(message="Ativo é obrigatório")
	private Logico ativo;
}

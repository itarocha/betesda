package br.com.itarocha.betesda.adapter.out.persistence.jpa.entity;

import br.com.itarocha.betesda.domain.enums.LogicoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name="user_token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Email(message = "Email inválido")
	@NotEmpty(message = "Email é obrigatório")
	private String email;
	
	@Size(min = 6, max=64, message = "O token deve ter entre 6 a 64 caracteres")
	@NotEmpty(message = "Token é obrigatório")
	private String token;
	
	@NotNull(message="Data Hora da Criação é obrigatória")
	private LocalDateTime dataHoraCriacao;
	
	@NotNull(message="Data Hora de Validade é obrigatória")
	private LocalDateTime dataHoraValidade;
	
	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(length=1)
	private LogicoEnum ativo = LogicoEnum.S;
}

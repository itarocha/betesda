package br.com.itarocha.betesda.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 64, message = "Nome deve ter no máximo 64 caracteres")
    private String name;

    @NotBlank(message = "Username é obrigatório")
    @Size(max = 32, message = "Username deve ter no máximo 32 caracteres")
    private String username;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 64, message = "Email deve ter no máximo 64 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 128, message = "Senha deve ter entre 6 e 128 caracteres")
    private String password;

    private Set<Long> roleIds;
}

package br.com.itarocha.betesda.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RedefinicaoSenha {
    @Email
    private String email;

    @Size(min = 6, max=64, message = "O token deve ter entre 6 a 64 caracteres")
    @NotEmpty(message = "Token é obrigatório")
    private String token;

    @Size(min = 6, max = 16)
    private String senha;

    @Size(min = 6, max = 16)
    private String senhaConfirmacao;
}

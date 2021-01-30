package br.com.itarocha.betesda.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UserLogin {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
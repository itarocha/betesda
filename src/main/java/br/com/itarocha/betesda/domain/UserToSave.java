package br.com.itarocha.betesda.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserToSave {
    @NotBlank
    @Size(min = 4, max = 64)
    private String name;

    @NotBlank
    @Size(min = 3, max = 32)
    private String username;

    @NotBlank
    @Size(max = 64)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 16)
    private String password;
}
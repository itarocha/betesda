package br.com.itarocha.betesda.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedefinicaoSenhaRequest {
    private String emailDestinatario;
    private String nome;
    private String token;
}

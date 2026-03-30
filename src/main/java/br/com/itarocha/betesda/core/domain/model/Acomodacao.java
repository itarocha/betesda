package br.com.itarocha.betesda.core.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Acomodacao {

    private Long id;
    private Long quartoId;
    private Integer quartoNumero;
    private Long leitoId;
    private Integer leitoNumero;
}
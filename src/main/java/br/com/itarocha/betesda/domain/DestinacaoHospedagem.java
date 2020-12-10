package br.com.itarocha.betesda.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DestinacaoHospedagem {
    private Long id;
    private String descricao;
}

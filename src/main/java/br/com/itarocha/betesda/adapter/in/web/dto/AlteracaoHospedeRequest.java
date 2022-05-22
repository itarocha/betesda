package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

@Data
public class AlteracaoHospedeRequest {
    private Long hospedeId;
    private Long tipoHospedeId;
}

package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

@Data
public class RemoverHospedeRequest {
    private Long hospedagemId;
    private Long hospedeId;
}

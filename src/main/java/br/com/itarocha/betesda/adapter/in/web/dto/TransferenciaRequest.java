package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TransferenciaRequest {
    private LocalDate data;
    private Long hospedeId;
    private Long leitoId;
}

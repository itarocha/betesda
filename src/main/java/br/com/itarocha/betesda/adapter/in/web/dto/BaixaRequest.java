package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BaixaRequest {
    private LocalDate data;
    private Long hospedeId;

}

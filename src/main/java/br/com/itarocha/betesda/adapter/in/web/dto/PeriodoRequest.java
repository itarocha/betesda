package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PeriodoRequest {
    private LocalDate dataIni;
    private LocalDate dataFim;
}

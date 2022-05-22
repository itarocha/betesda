package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OperacoesRequest {
    public LocalDate data;
    public Long hospedagemId;
}

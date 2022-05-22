package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AdicionarHospedeRequest {
    private Long hospedagemId;
    private Long pessoaId;
    private Long tipoHospedeId;
    private LocalDate data;
    private Long leitoId;
}

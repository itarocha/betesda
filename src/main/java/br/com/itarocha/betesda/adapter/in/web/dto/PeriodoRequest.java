package br.com.itarocha.betesda.adapter.in.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class PeriodoRequest {
    @NotNull(message = "Data inicial precisa ser preenchida")
    private LocalDate dataIni;

    @NotNull(message = "Data final precisa ser preenchida")
    private LocalDate dataFim;
}

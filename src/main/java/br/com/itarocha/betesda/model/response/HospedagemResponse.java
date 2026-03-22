package br.com.itarocha.betesda.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HospedagemResponse implements Serializable {
    private Long id;
    private Long entidadeId;
    private Long encaminhadorId;
    private Long destinacaoHospedagemId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEntrada;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPrevistaSaida;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEfetivaSaida;
    private String tipoUtilizacao;
    private String observacoes;
}

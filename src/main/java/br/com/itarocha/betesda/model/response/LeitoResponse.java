package br.com.itarocha.betesda.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LeitoResponse implements Serializable {
    private Long id;
    private Long quartoId;
    private Integer numero;
    private Long tipoLeitoId;
    private Long situacaoId;
}

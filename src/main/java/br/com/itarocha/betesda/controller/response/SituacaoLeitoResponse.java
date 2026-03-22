package br.com.itarocha.betesda.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SituacaoLeitoResponse implements Serializable {
    private Long id;
    private String descricao;
    private String disponivel;
}

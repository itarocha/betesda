package br.com.itarocha.betesda.controller.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EncaminhadorResponse implements Serializable {
    private Long id;
    private Long entidadeId;
    private String nome;
    private String cargo;
    private String telefone;
    private String email;
    private String ativo;
}

package br.com.itarocha.betesda.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EntidadeResponse implements Serializable {
    private Long id;
    private String nome;
    private String cnpj;
    private Long enderecoId;
    private String telefone;
    private String telefone2;
    private String email;
    private String observacoes;
}

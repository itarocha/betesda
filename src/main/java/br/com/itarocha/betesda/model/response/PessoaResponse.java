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
public class PessoaResponse implements Serializable {
    private Long id;
    private String nome;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    private String sexo;
    private String estadoCivil;
    private String cpf;
    private String cartaoSus;
    private String rg;
    private String naturalidadeCidade;
    private String naturalidadeUf;
    private String nacionalidade;
    private String profissao;
    private Long enderecoId;
    private String telefone;
    private String telefone2;
    private String email;
    private String observacoes;
}

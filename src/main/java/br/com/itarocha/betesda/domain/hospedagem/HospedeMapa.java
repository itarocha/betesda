package br.com.itarocha.betesda.domain.hospedagem;

import br.com.itarocha.betesda.application.MapaHospedagemUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class HospedeMapa {
    private Long id;
    private String tipoUtilizacao;
    private Long quartoId;
    private Integer quartoNumero;
    private Long leitoId;
    private Integer leitoNumero;
    private Long pessoaId;
    private String pessoaNome;
    private String pessoaTelefone;
    private String cidade;
    private String uf;
    private LocalDate dataEntradaHospedagem;
    private LocalDate dataSaidaHospedagem;
    private LocalDate dataPrimeiraEntrada;
    private LocalDate dataUltimaEntrada;
    private LocalDate dataEntradaLeito;
    private LocalDate dataSaidaLeito;
    private LocalDate dataIniNoPeriodo;
    private LocalDate dataFimNoPeriodo;
    private Long hospedagemId;
    private Long hospedeId;
    private Long tipoHospedeId;
    private String baixadoString;
    private String tipoHospedeDescricao;
    private Long destinacaoHospedagemId;
    private String destinacaoHospedagemDescricao;
    private LocalDate dataPrevistaSaida;
    private LocalDate dataEfetivaSaida;

    private Integer[] dias;

    @JsonProperty("identificador")
    public String getIdentificador()
    {
        // T000000 Total
        return String.format("%s%06d", this.getTipoUtilizacao(), this.getId());
    }

    @JsonProperty("tipoUtilizacaoDescricao")
    public String getTipoUtilizacaoDescricao(){
        return "T".equalsIgnoreCase(tipoUtilizacao) ? "Total" : "Parcial";
    }

    @JsonProperty("pessoaCidadeUfOrigem")
    public String getPessoaCidadeUfOrigem(){
        return String.format("%s - %s", this.getCidade(), this.getUf());
    }

    @JsonProperty("baixado")
    public Boolean getBaixado(){
        return "S".equals(this.getBaixadoString());
    }

    @JsonProperty("statusHospedagem")
    public CellStatusHospedagem getStatusHospedagem()
    {
        return MapaHospedagemUtil.resolveStatusHospedagemNew(LocalDate.now(), this.getDataPrevistaSaida(), this.getDataEfetivaSaida());
    }
}
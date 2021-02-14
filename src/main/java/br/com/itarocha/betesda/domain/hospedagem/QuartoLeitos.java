package br.com.itarocha.betesda.domain.hospedagem;

public class QuartoLeitos {
    private Long id;
    private Integer numero;
    private MinMax rangeLeitos;

    public QuartoLeitos(Long id, Integer numero, Integer leitoMin, Integer leitoMax) {
        this.id = id;
        this.numero = numero;
        this.rangeLeitos = new MinMax(leitoMin, leitoMax);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public Integer getMinimum() {
        return rangeLeitos.getMinimum();
    }

    public Integer getMaximum() {
        return rangeLeitos.getMaximum();
    }
}

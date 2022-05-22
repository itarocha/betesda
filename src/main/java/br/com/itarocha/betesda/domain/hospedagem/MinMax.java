package br.com.itarocha.betesda.domain.hospedagem;

public class MinMax {
    private Integer minimum;
    private Integer maximum;

    public MinMax(Integer minimum, Integer maximum){
        this.minimum = minimum;
        this.maximum = maximum;
    }
    public Integer getMinimum() {
        return minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }
}

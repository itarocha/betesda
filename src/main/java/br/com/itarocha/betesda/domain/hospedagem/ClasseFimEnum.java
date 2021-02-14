package br.com.itarocha.betesda.domain.hospedagem;

public enum ClasseFimEnum {

    INDEFINIDO("???"),
    INDO("indo"),
    BAIXADO("baixado"),
    DURANTE("durante"),
    FIM("fim");

    private String descricao;

    ClasseFimEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

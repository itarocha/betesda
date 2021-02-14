package br.com.itarocha.betesda.domain.hospedagem;

public enum ClasseInicioEnum {

    INDEFINIDO("???"),
    VINDO("vindo"),
    INICIO("inicio"),
    DURANTE("durante");

    private String descricao;

    ClasseInicioEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

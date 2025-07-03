package com.recsys.recPet.enums.animal;

public enum Porte {
    PEQUENO("Pequeno"),
    MEDIO("Médio"),
    GRANDE("Grande"),
    GIGANTE("Gigante");

    private final String nomeExibicao;

    Porte (String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getValorAcentuado() {
        return this.nomeExibicao;
    }
}
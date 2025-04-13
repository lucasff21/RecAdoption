package com.recsys.recPet.enums.pet;

public enum Pelagem {
    CURTA("Curta"),
    MEDIA("Média"),
    LONGA("Longa"),
    ENCARACOLADA("Encaracolada"),
    DURA("Dura"),
    SEDOSA("Sedosa"),
    LANOSA("Lanosa");

    private final String nomeExibicao;

    Pelagem(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getValorAcentuado() {
        return this.nomeExibicao;
    }
}

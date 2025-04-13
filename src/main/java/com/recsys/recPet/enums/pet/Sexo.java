package com.recsys.recPet.enums.pet;

public enum Sexo {
    MACHO("Macho"),
    FEMEA("Fêmea");

    private final String nomeExibicao;

    Sexo(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getValorAcentuado() {
        return this.nomeExibicao;
    }
}

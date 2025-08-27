package com.recsys.recPet.enums.animal;

import lombok.Getter;

@Getter
public enum Sexo {
    MACHO("Macho"),
    FEMEA("Fêmea");

    private final String nomeExibicao;

    Sexo(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public static Sexo fromString(String texto) {
        if (texto == null) {
            return null;
        }
        for (Sexo s : Sexo.values()) {
            if (s.name().equalsIgnoreCase(texto)) {
                return s;
            }
        }
        return null;
    }
}

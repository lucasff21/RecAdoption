package com.recsys.recPet.enums.animal;

import lombok.Getter;

@Getter
public enum Porte {
    PEQUENO("Pequeno"),
    MEDIO("Médio"),
    GRANDE("Grande"),
    GIGANTE("Gigante");

    private final String nomeExibicao;

    Porte (String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public static Porte fromString(String texto) {
        if (texto == null) {
            return null;
        }
        for (Porte p : Porte.values()) {
            if (p.name().equalsIgnoreCase(texto)) {
                return p;
            }
        }
        return null;
    }
}
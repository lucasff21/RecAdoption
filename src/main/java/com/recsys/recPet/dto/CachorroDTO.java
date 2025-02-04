package com.recsys.recPet.dto;

import java.util.List;

public record CachorroDTO(String nome,
                          String idade,
                          String sexo,
                          String porte,
                          String pelagem,
                          Boolean idealCasa,
                          Boolean gostaCrianca,
                          Boolean caoGuarda,
                          Boolean brincalhao,
                          Boolean necessidadeCorrer,
                          Boolean quedaPelo,
                          Boolean tendeLatir,
                          String imagePath,

                                  List<Long>adocoes


) {
}

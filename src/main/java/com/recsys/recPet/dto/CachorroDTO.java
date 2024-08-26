package com.recsys.recPet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachorroDTO {

    private String nome;
    private String idade;
    private String sexo;
    private String porte;
    private String pelagem;

    private Boolean idealCasa;
    private Boolean gostaCrianca;
    private Boolean caoGuarda;
    private Boolean brincalhao;
    private Boolean necessidadeCorrer;
    private Boolean quedaPelo;
    private Boolean tendeLatir;
}

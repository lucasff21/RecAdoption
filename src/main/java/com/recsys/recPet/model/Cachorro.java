package com.recsys.recPet.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cachorro extends Animal {

    private Boolean idealCasa;
    private Boolean gostaCrianca;
    private Boolean caoGuarda;
    private Boolean brincalhao;
    private Boolean necessidadeCorrer;
    private Boolean quedaPelo;
    private Boolean tendeLatir;
}

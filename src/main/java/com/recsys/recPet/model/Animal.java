package com.recsys.recPet.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Animal {

    private Long id;

    private String nome;
    private String idade;
    private String sexo;
    private String porte;
    private String pelagem;



}

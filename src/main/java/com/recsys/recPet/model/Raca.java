package com.recsys.recPet.model;

import com.recsys.recPet.enums.animal.Tipo;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "racas")
public class Raca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Tipo especie;
}
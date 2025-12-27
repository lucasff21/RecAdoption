package com.recsys.recPet.model;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "cores")
public class Cor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
}
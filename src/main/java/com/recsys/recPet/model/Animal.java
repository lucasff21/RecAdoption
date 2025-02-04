package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String idade;
    private String sexo;
    private String porte;
    private String pelagem;
    private String imagePath;
    //private String tipoAnimal;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Adocao> adocoes;

}

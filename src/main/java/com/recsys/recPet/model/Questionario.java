package com.recsys.recPet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Questionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

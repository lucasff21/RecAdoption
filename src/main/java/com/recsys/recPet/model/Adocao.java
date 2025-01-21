package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Adocao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dataAdocao;
    private String status;


    @JsonBackReference
    @ManyToMany(mappedBy = "adocoes", fetch = FetchType.EAGER)
    private Set<User> users = new HashSet<>();


    @OneToOne
    private Animal animal;
}

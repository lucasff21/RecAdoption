package com.recsys.recPet.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Adocao {

    private Long id;
    private LocalDate dataAdocao;
    private String status;
    private Usuario usuario;
    private Animal animal;
}

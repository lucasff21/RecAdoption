package com.recsys.recPet.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalCaracteristicaId implements Serializable {
    private Long animalId;
    private Integer caracteristicaId;
}
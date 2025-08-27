package com.recsys.recPet.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalCaracteristicaId implements Serializable {
    private Long animalId;
    private Long caracteristicaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimalCaracteristicaId that = (AnimalCaracteristicaId) o;
        return Objects.equals(animalId, that.animalId) &&
                Objects.equals(caracteristicaId, that.caracteristicaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalId, caracteristicaId);
    }
}
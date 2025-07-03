package com.recsys.recPet.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animais_caracteristicas")
public class AnimalCaracteristica {

    @EmbeddedId
    private AnimalCaracteristicaId id;

    @ManyToOne
    @MapsId("animalId")
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ManyToOne
    @MapsId("caracteristicaId")
    @JoinColumn(name = "caracteristica_id")
    private Caracteristica caracteristica;

    @CreatedDate
    private LocalDateTime createdAt;
}

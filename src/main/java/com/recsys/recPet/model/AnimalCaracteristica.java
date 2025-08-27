package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animais_caracteristicas")
@EqualsAndHashCode(of = "id")
public class AnimalCaracteristica {

    @EmbeddedId
    private AnimalCaracteristicaId id;

    @ManyToOne
    @MapsId("animalId")
    @JoinColumn(name = "animal_id")
    @JsonBackReference("animal-caracteristica-ref")
    private Animal animal;

    @ManyToOne
    @MapsId("caracteristicaId")
    @JoinColumn(name = "caracteristica_id")
    private Caracteristica caracteristica;

    @CreationTimestamp
    private LocalDateTime createdAt;
}

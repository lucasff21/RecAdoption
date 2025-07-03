package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animais")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataNascimentoAproximada;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Tipo tipo;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Sexo sexo;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Porte porte;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Pelagem pelagem;

    private String imagemPath;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Adocao> adocoes;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AnimalCaracteristica> animalCaracteristicas;


    public void addAnimalCaracteristica(AnimalCaracteristica ac) {
        if (animalCaracteristicas == null) {
            animalCaracteristicas = new HashSet<>();
        }
        animalCaracteristicas.add(ac);
        ac.setAnimal(this);
        ac.setId(new AnimalCaracteristicaId(this.id, ac.getCaracteristica().getId()));
    }

    public void removeAnimalCaracteristica(AnimalCaracteristica ac) {
        if (animalCaracteristicas != null) {
            animalCaracteristicas.remove(ac);
            ac.setAnimal(null);
            ac.setId(null);
        }
    }
}

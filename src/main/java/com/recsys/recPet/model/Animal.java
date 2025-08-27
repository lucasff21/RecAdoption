package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
import jakarta.persistence.*;
import lombok.*;
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
@EqualsAndHashCode(of = "id")
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

    private Boolean disponivelParaAdocao;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Adocao> adocoes;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("animal-caracteristica-ref")
    @ToString.Exclude
    private Set<AnimalCaracteristica> animalCaracteristicas  = new HashSet<>();

    public void addAnimalCaracteristica(AnimalCaracteristica associacao) {
        this.animalCaracteristicas.add(associacao);
        associacao.setAnimal(this);
    }

    public void removeAnimalCaracteristica(AnimalCaracteristica associacao) {
        this.animalCaracteristicas.remove(associacao);
        associacao.setAnimal(null);
        associacao.setId(null);
    }
}

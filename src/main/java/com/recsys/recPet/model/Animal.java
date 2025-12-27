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

    @Column(name = "castrado", nullable = false)
    private boolean castrado;

    @Column(name = "data_ultima_vermifugacao")
    private LocalDate dataUltimaVermifugacao;

    @Column(name = "data_ultima_vacina_antirrabica")
    private LocalDate dataUltimaVacinaAntirrabica;

    @Column(name = "data_ultima_vacina_multipla")
    private LocalDate dataUltimaVacinaMultipla;

    @Column(name = "tipo_vacina_multipla", length = 10)
    private String tipoVacinaMultipla;

    @Column(name = "observacoes_medicas", columnDefinition = "TEXT")
    private String observacoesMedicas;

    @Column(name = "rg_animal", length = 10)
    private String rgAnimal;

    @Column(name ="microchip_id", length = 20)
    private String microchipId;

    @Column(name = "observacoes_privadas", columnDefinition = "TEXT")
    private String observacoesPrivadas;

    @ManyToOne
    @JoinColumn(name = "raca_id")
    private Raca raca;

    @ManyToOne
    @JoinColumn(name = "cor_id")
    private Cor cor;

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

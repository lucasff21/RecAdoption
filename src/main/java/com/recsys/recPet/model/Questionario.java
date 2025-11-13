package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recsys.recPet.enums.questionario.QuestionarioMoradia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questionarios")
public class Questionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "moradia")
    private QuestionarioMoradia moradia;

    @Column(name = "telas_protecao")
    private Boolean telasProtecao;

    @Column(name = "todos_de_acordo")
    private Boolean todosDeAcordo;

    @Column(name = "qtd_caes")
    private Integer qtdCaes;

    @Column(name = "qtd_gatos")
    private Integer qtdGatos;

    @Column(name = "qtd_outros")
    private Integer qtdOutros;

    @Column(name = "ciente_custos")
    private Boolean cienteCustos;

    @Column(name = "termo_compromisso_longo_prazo")
    private Boolean termoCompromissoLongoPrazo;

    @Column(name = "termo_saude_bem_estar")
    private Boolean termoSaudeBemEstar;

    @Column(name = "termo_paciencia_adaptacao")
    private Boolean termoPacienciaAdaptacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.recsys.recPet.enums.questionario.QuestionarioMoradia;
import com.recsys.recPet.enums.questionario.QuestionarioPreferenciaSexo;
import com.recsys.recPet.enums.questionario.QuestionarioTemCriancas;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User user;

    // --- Preferências Categóricas ---

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "moradia")
    private QuestionarioMoradia moradia;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "sexo")
    private QuestionarioPreferenciaSexo preferenciaSexo;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "tem_criancas")
    private QuestionarioTemCriancas temCriancas;


    // --- Escalas Numéricas ---
    @Column(name = "porte")
    private Integer preferenciaPorte;

    @Column(name = "nivel_queda_pelo")
    private Integer nivelQuedaPelo;

    @Column(name = "nivel_latido")
    private Integer nivelLatido;

    @Column(name = "instinto_guarda")
    private Integer instintoGuarda;

    @Column(name = "nivel_energia")
    private Integer nivelEnergia;

    @Column(name = "tempo_disponivel")
    private Integer tempoDisponivel;

    @Column(name = "experiencia_pets")
    private Integer experienciaPets;

    // --- Contexto ---
    @Column(name = "disposicao_necessidades_especiais")
    private Boolean disposicaoNecessidadesEspeciais;

    @Column(name = "possui_gatos")
    private Boolean possuiGatos;

    @Column(name = "possui_caes")
    private Boolean possuiCaes;

    // --- Termos Existentes ---
    @Column(name = "ciente_custos")
    private Boolean cienteCustos;

    @Column(name = "termo_compromisso_longo_prazo")
    private Boolean termoCompromissoLongoPrazo;

    @Column(name = "termo_saude_bem_estar")
    private Boolean termoSaudeBemEstar;

    @Column(name = "termo_paciencia_adaptacao")
    private Boolean termoPacienciaAdaptacao;

    // --- NOVOS TERMOS ADICIONADOS ---

    // Vistoria
    @Column(name = "termo_vistoria")
    private Boolean termoVistoria;

    // Não abandonar / Devolver
    @Column(name = "termo_devolucao_nao_abandono")
    private Boolean termoDevolucaoNaoAbandono;

    // Legislação / Posse Responsável
    @Column(name = "termo_legislacao_posse_responsavel")
    private Boolean termoLegislacaoPosseResponsavel;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
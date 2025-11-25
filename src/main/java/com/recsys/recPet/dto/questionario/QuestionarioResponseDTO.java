package com.recsys.recPet.dto.questionario;

import com.recsys.recPet.enums.questionario.QuestionarioMoradia;
import com.recsys.recPet.enums.questionario.QuestionarioPreferenciaSexo;
import com.recsys.recPet.enums.questionario.QuestionarioTemCriancas;
import com.recsys.recPet.model.Questionario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionarioResponseDTO {

    private Long id;
    private Long usuarioId; // É útil retornar o ID do usuário dono do questionário

    // --- Preferências e Enums ---
    private QuestionarioPreferenciaSexo preferenciaSexo;
    private QuestionarioTemCriancas temCriancas; // Ajustado para bater com a Entidade

    // --- Escalas Numéricas ---
    private Integer preferenciaPorte;
    private Integer nivelQuedaPelo;
    private Integer nivelLatido;
    private Integer instintoGuarda;
    private Integer nivelEnergia;
    private QuestionarioMoradia moradia;
    private Integer tempoDisponivel;
    private Integer experienciaPets;

    // --- Contexto ---
    private Boolean disposicaoNecessidadesEspeciais;
    private Boolean possuiGatos;
    private Boolean possuiCaes;

    // --- Termos de Compromisso (Essenciais para auditoria) ---
    private Boolean cienteCustos;
    private Boolean termoCompromissoLongoPrazo;
    private Boolean termoSaudeBemEstar;
    private Boolean termoPacienciaAdaptacao;

    // --- Novos Termos Legais ---
    private Boolean termoVistoria;
    private Boolean termoDevolucaoNaoAbandono;
    private Boolean termoLegislacaoPosseResponsavel;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuestionarioResponseDTO(Questionario questionario) {
        this.id = questionario.getId();
        this.usuarioId = questionario.getUser().getId();

        this.preferenciaSexo = questionario.getPreferenciaSexo();
        this.temCriancas = questionario.getTemCriancas();

        this.preferenciaPorte = questionario.getPreferenciaPorte();
        this.nivelQuedaPelo = questionario.getNivelQuedaPelo();
        this.nivelLatido = questionario.getNivelLatido();
        this.instintoGuarda = questionario.getInstintoGuarda();
        this.nivelEnergia = questionario.getNivelEnergia();
        this.moradia = questionario.getMoradia();
        this.tempoDisponivel = questionario.getTempoDisponivel();
        this.experienciaPets = questionario.getExperienciaPets();

        this.disposicaoNecessidadesEspeciais = questionario.getDisposicaoNecessidadesEspeciais();
        this.possuiGatos = questionario.getPossuiGatos();
        this.possuiCaes = questionario.getPossuiCaes();

        this.cienteCustos = questionario.getCienteCustos();
        this.termoCompromissoLongoPrazo = questionario.getTermoCompromissoLongoPrazo();
        this.termoSaudeBemEstar = questionario.getTermoSaudeBemEstar();
        this.termoPacienciaAdaptacao = questionario.getTermoPacienciaAdaptacao();

        this.termoVistoria = questionario.getTermoVistoria();
        this.termoDevolucaoNaoAbandono = questionario.getTermoDevolucaoNaoAbandono();
        this.termoLegislacaoPosseResponsavel = questionario.getTermoLegislacaoPosseResponsavel();

        this.createdAt = questionario.getCreatedAt();
        this.updatedAt = questionario.getUpdatedAt();
    }
}
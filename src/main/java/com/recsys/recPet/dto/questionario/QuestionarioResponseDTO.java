package com.recsys.recPet.dto.questionario;

import com.recsys.recPet.enums.questionario.QuestionarioMoradia;
import com.recsys.recPet.model.Questionario;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuestionarioResponseDTO {

    private Long id;
    private QuestionarioMoradia moradia;
    private Boolean telasProtecao;
    private Boolean todosDeAcordo;
    private Integer qtdCaes;
    private Integer qtdGatos;
    private Integer qtdOutros;
    private Boolean cienteCustos;
    private Boolean termoCompromissoLongoPrazo;
    private Boolean termoSaudeBemEstar;
    private Boolean termoPacienciaAdaptacao;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QuestionarioResponseDTO(Questionario questionario) {
        this.id = questionario.getId();
        this.moradia = questionario.getMoradia();
        this.telasProtecao = questionario.getTelasProtecao();
        this.todosDeAcordo = questionario.getTodosDeAcordo();
        this.qtdCaes = questionario.getQtdCaes();
        this.qtdGatos = questionario.getQtdGatos();
        this.qtdOutros = questionario.getQtdOutros();
        this.cienteCustos = questionario.getCienteCustos();
        this.termoCompromissoLongoPrazo = questionario.getTermoCompromissoLongoPrazo();
        this.termoSaudeBemEstar = questionario.getTermoSaudeBemEstar();
        this.termoPacienciaAdaptacao = questionario.getTermoPacienciaAdaptacao();
        this.createdAt = questionario.getCreatedAt();
        this.updatedAt = questionario.getUpdatedAt();
    }
}
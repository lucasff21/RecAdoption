package com.recsys.recPet.dto.questionario;

import com.recsys.recPet.enums.questionario.QuestionarioMoradia;
import com.recsys.recPet.enums.questionario.QuestionarioPreferenciaSexo;
import com.recsys.recPet.enums.questionario.QuestionarioTemCriancas;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionarioCreateDTO {
    @NotNull
    private QuestionarioPreferenciaSexo preferenciaSexo;

    @NotNull
    private QuestionarioTemCriancas temCriancas;

    @NotNull(message = "O tipo de moradia é obrigatório")
    private QuestionarioMoradia moradia;

    @NotNull @Min(1) @Max(3)
    private Integer preferenciaPorte;

    @NotNull @Min(1) @Max(5)
    private Integer nivelQuedaPelo;

    @NotNull @Min(1) @Max(5)
    private Integer nivelLatido;

    @NotNull @Min(1) @Max(5)
    private Integer instintoGuarda;

    @NotNull @Min(1) @Max(5)
    private Integer nivelEnergia;

    @NotNull @Min(1) @Max(5)
    private Integer tempoDisponivel;

    @NotNull @Min(1) @Max(5)
    private Integer experienciaPets;

    @NotNull
    private Boolean disposicaoNecessidadesEspeciais;

    @NotNull
    private Boolean possuiGatos;

    @NotNull
    private Boolean possuiCaes;

    @NotNull(message = "A informação sobre custos é obrigatória")
    private Boolean cienteCustos;

    @NotNull(message = "O termo 'compromisso longo prazo' é obrigatório")
    private Boolean termoCompromissoLongoPrazo;

    @NotNull(message = "O termo 'saúde e bem-estar' é obrigatório")
    private Boolean termoSaudeBemEstar;

    @NotNull(message = "O termo 'paciência e adaptação' é obrigatório")
    private Boolean termoPacienciaAdaptacao;

    @NotNull
    private Boolean termoVistoria;

    @NotNull
    private Boolean termoDevolucaoNaoAbandono;

    @NotNull
    private Boolean termoLegislacaoPosseResponsavel;
}
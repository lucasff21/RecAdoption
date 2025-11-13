package com.recsys.recPet.dto.questionario;

import com.recsys.recPet.enums.questionario.QuestionarioMoradia;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionarioCreateDTO {

    @NotNull(message = "O tipo de moradia é obrigatório")
    private QuestionarioMoradia moradia;

    @NotNull(message = "A informação sobre telas de proteção é obrigatória")
    private Boolean telasProtecao;

    @NotNull(message = "A informação sobre acordo familiar é obrigatória")
    private Boolean todosDeAcordo;

    @NotNull(message = "A quantidade de cães é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Integer qtdCaes;

    @NotNull(message = "A quantidade de gatos é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Integer qtdGatos;

    @NotNull(message = "A quantidade de outros animais é obrigatória")
    @Min(value = 0, message = "A quantidade não pode ser negativa")
    private Integer qtdOutros;

    @NotNull(message = "A informação sobre custos é obrigatória")
    private Boolean cienteCustos;

    @NotNull(message = "O termo 'compromisso longo prazo' é obrigatório")
    private Boolean termoCompromissoLongoPrazo;

    @NotNull(message = "O termo 'saúde e bem-estar' é obrigatório")
    private Boolean termoSaudeBemEstar;

    @NotNull(message = "O termo 'paciência e adaptação' é obrigatório")
    private Boolean termoPacienciaAdaptacao;
}
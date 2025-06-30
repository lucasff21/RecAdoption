package com.recsys.recPet.dto.admin;

import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.Questionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAdocaoDTO {
    private Adocao adocao;
    private Questionario userQuestionario;
}
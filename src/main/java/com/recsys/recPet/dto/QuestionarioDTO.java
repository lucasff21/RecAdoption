package com.recsys.recPet.dto;

import com.recsys.recPet.model.User;

public record QuestionarioDTO(
        Long id,
        String sexo,
        String porte,
        String pelagem,
        Boolean idealCasa,
        Boolean gostaCrianca,
        Boolean caoGuarda,
        Boolean brincalhao,
        Boolean necessidadeCorrer,
        Boolean quedaPelo,
        Boolean tendeLatir,
        User user
) {
}

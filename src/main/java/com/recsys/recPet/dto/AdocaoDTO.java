package com.recsys.recPet.dto;

import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.User;

import java.time.LocalDate;

public record AdocaoDTO(
        Long id,
        LocalDate dataAdocao,
        String status,
        User user,
        Animal animal
) {
}

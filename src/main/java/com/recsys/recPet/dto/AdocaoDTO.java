package com.recsys.recPet.dto;

import java.time.LocalDate;

public record AdocaoDTO(
        Long id,
        LocalDate dataAdocao,
        String status,
        Long animalId
) {
}

package com.recsys.recPet.dto;

import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.Animal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdocaoResponseDTO {
    private Long id;
    private LocalDate dataAdocao;
    private AdocaoStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Animal animal;

    public AdocaoResponseDTO(Adocao adocao) {
        this.id = adocao.getId();
        this.dataAdocao = adocao.getDataAdocao();
        this.status = adocao.getStatus();
        this.animal = adocao.getAnimal();
    }
}
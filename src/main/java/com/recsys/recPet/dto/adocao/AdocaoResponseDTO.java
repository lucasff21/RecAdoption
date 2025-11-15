package com.recsys.recPet.dto.adocao;

import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdocaoResponseDTO {
    private Long id;
    private LocalDateTime concluidoEm;
    private AdocaoStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private AnimalResponseDTO animal;

    protected void mapFromEntity(Adocao adocao) {
        this.id = adocao.getId();
        this.concluidoEm = adocao.getConcluidoEm();
        this.status = adocao.getStatus();
        this.animal = AnimalResponseDTO.fromEntity(adocao.getAnimal());
        this.createdAt = adocao.getCreatedAt();
        this.updatedAt = adocao.getUpdatedAt();
    }

    public static AdocaoResponseDTO fromEntity(Adocao adocao) {
        if (adocao == null) {
            return null;
        }

        AdocaoResponseDTO adocaoResponseDTO = new AdocaoResponseDTO();
        adocaoResponseDTO.mapFromEntity(adocao);
        return adocaoResponseDTO;
    }
}
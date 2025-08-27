package com.recsys.recPet.dto.adocao;

import com.recsys.recPet.dto.usuario.UsuarioLoginResponseDTO;
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
    private UsuarioLoginResponseDTO usuario;


    public static AdocaoResponseDTO fromEntity(Adocao adocao) {
        if (adocao == null) {
            return null;
        }

        AdocaoResponseDTO adocaoResponseDTO = new AdocaoResponseDTO();
        adocaoResponseDTO.setId(adocao.getId());
        adocaoResponseDTO.setConcluidoEm(adocao.getConcluidoEm());
        adocaoResponseDTO.setStatus(adocao.getStatus());
        adocaoResponseDTO.setAnimal(AnimalResponseDTO.fromEntity(adocao.getAnimal()));
        adocaoResponseDTO.setCreatedAt(adocao.getCreatedAt());
        adocaoResponseDTO.setUpdatedAt(adocao.getUpdatedAt());
        adocaoResponseDTO.setUsuario(UsuarioLoginResponseDTO.fromEntity(adocao.getUser()));

        return adocaoResponseDTO;
    }
}
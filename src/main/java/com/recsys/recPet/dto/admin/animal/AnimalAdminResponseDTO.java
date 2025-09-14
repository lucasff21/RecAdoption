package com.recsys.recPet.dto.admin.animal;

import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.model.Animal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AnimalAdminResponseDTO extends AnimalResponseDTO {
    private List<AdocaoResponseDTO> adocoes;

    public AnimalAdminResponseDTO(Animal animal) {
        super(animal);

        if (animal.getAdocoes() != null) {
            this.adocoes = animal.getAdocoes().stream()
                    .map(AdocaoResponseDTO::fromEntity)
                    .collect(Collectors.toList());
        } else {
            this.adocoes = List.of();
        }
    }

    public static AnimalAdminResponseDTO fromEntity(Animal animal) {
        if (animal == null) {
            return null;
        }
        return new AnimalAdminResponseDTO(animal);
    }
}

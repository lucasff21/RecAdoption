package com.recsys.recPet.dto.animal;

import com.recsys.recPet.model.Caracteristica;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class CaracteristicaDTO {
    private Boolean ativo;
    private  Long id;
    private String nome;
    private String descricao;

    public static CaracteristicaDTO fromEntity(Caracteristica caracteristica) {
        if (caracteristica == null) {
            return null;
        }
        CaracteristicaDTO dto = new CaracteristicaDTO();
        dto.setId(caracteristica.getId());
        dto.setAtivo(caracteristica.getAtivo());
        dto.setNome(caracteristica.getNome());
        dto.setDescricao(caracteristica.getDescricao());
        return dto;
    }
}
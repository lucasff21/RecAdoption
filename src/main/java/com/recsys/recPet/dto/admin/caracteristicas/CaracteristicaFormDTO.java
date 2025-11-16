package com.recsys.recPet.dto.admin.caracteristicas;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaracteristicaFormDTO {
    private String nome;
    private String descricao;
    private Boolean ativo;
}

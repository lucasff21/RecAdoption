package com.recsys.recPet.dto;

import com.recsys.recPet.dto.animal.CaracteristicaDTO;
import com.recsys.recPet.model.Cor;
import com.recsys.recPet.model.Raca;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AnimalFiltrosDTO {
    private List<CaracteristicaDTO> caracteristicas;
    private List<Cor> cores;
    private List<Raca> racas;
}

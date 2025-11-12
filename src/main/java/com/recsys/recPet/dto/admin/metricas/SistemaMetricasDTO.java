package com.recsys.recPet.dto.admin.metricas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SistemaMetricasDTO {
    private AdocaoMetricasDTO adocoes;
    private AnimalMetricasDTO animais;
    private UsuarioMetricasDTO usuarios;
}
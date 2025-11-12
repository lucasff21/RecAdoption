package com.recsys.recPet.dto.admin.metricas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalMetricasDTO {
    private long totalAnimais;
    private long disponiveis;
    private long naoDisponiveis;
    private long totalCachorros;
    private long totalGatos;
}
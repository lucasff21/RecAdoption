package com.recsys.recPet.dto.admin.metricas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdocaoMetricasDTO {
    private long totalSolicitacoes;
    private long pendentes;
    private long emAnalise;
    private long aprovadas;
    private long recusadas;
    private long finalizadas;
}

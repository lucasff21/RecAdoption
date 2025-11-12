package com.recsys.recPet.dto.admin.metricas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioMetricasDTO {
    private long totalUsuarios;
    private long totalAdotantes;
    private long totalAdmins;
    private long totalModeradores;
}

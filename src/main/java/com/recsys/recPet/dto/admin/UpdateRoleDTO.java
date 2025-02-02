package com.recsys.recPet.dto.admin;

import com.recsys.recPet.enums.TipoUsuario;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDTO {
    @NotNull(message = "Tipo é obrigatório")
    public TipoUsuario tipo;
}

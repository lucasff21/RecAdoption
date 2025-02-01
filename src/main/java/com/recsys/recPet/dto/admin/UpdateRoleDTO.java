package com.recsys.recPet.dto.admin;

import com.recsys.recPet.enums.TipoUsuario;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateRoleDTO {
    @NotBlank(message = "Tipo é obrigatório")
    public TipoUsuario tipo;
}

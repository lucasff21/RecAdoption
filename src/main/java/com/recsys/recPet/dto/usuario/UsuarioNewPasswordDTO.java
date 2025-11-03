package com.recsys.recPet.dto.usuario;

import jakarta.validation.constraints.NotBlank;

public record UsuarioNewPasswordDTO(

        @NotBlank(message = "O token é obrigatório.")
        String token,

        @NotBlank(message = "A senha é obrigatória.")
        String password
) {
}
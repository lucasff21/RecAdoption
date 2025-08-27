package com.recsys.recPet.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDTO {
    private UsuarioResponseDTO user;
    private String token;
}
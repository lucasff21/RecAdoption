package com.recsys.recPet.dto;

import com.recsys.recPet.enums.TipoUsuario;

public record CreateUserDTO(String email, String password, TipoUsuario tipoUsuario) {
}

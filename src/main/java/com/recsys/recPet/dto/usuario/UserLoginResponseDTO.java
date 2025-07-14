package com.recsys.recPet.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDTO {
    private UserResponseDTO user;
    private String token;
}
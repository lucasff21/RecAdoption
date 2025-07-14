package com.recsys.recPet.dto.usuario;

import com.recsys.recPet.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UsuarioLoginResponseDTO {
    private Long id;
    private String nome;
    private String email;

    public static UsuarioLoginResponseDTO fromEntity(User user) {
        if (user == null) return null;

        UsuarioLoginResponseDTO dto = new UsuarioLoginResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
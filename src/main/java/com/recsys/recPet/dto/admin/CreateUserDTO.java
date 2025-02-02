package com.recsys.recPet.dto.admin;

import com.recsys.recPet.enums.TipoUsuario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreateUserDTO {
    @NotBlank(message = "Nome é obrigatório")
    public String nome;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    public String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    public String senha;

    @NotNull(message = "Tipo é obrigatório")
    public TipoUsuario tipo;
}

package com.recsys.recPet.dto.pagina;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginaRequestDTO {

    @NotBlank(message = "O nome da página não pode ser vazio.")
    @Size(min = 3, max = 255, message = "O nome deve ter entre 3 e 255 caracteres.")
    private String nome;

    @NotBlank(message = "O título não pode ser vazio.")
    @Size(max = 255, message = "O título não pode exceder 255 caracteres.")
    private String titulo;

    private String conteudo;
}
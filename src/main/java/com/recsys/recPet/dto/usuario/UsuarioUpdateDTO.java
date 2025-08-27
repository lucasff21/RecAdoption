package com.recsys.recPet.dto.usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioUpdateDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    private String nome;

    private String telefone;

    private String genero;

    @Past(message = "A data de nascimento deve ser uma data no passado")
    private LocalDate dataNascimento;

    @Valid
    private EnderecoDTO endereco;
}

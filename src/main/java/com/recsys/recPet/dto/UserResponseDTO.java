package com.recsys.recPet.dto;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.Endereco;
import com.recsys.recPet.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String email;
    private TipoUsuario tipoUsuario;
    private String nome;
    private String telefone;
    private String genero;
    private LocalDate dataNascimento;
    private String cpf;
    private Endereco endereco;
    private LocalDateTime createdAt;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.tipoUsuario = user.getTipo();
        this.nome = user.getNome();
        this.telefone = user.getTelefone();
        this.genero = user.getGenero();
        this.dataNascimento = user.getDataNascimento();
        this.cpf = user.getCpf();
        this.endereco = user.getEndereco();
        this.createdAt = user.getCreatedAt();
    }
}

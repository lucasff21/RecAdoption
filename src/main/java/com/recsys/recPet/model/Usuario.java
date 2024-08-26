package com.recsys.recPet.model;

import com.recsys.recPet.enums.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Usuario {

    private Long id;

    private String senha;
    private String email;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    private String nome;
    private String telefone;
    private String genero;
    private LocalDate dataNascimento;
    private String cpf;


    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Endereco endereco;
}

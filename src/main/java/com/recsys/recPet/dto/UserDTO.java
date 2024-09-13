package com.recsys.recPet.dto;

import com.recsys.recPet.enums.TipoUsuario;

import java.time.LocalDate;
import java.util.Set;

public record UserDTO(Long id,
                      String password,
                      String email,
                      Set<TipoUsuario> tipoUsuario,
                      String nome,
                      String telefone,
                      String genero,
                      LocalDate dataNascimento,
                      String cpf) {
}
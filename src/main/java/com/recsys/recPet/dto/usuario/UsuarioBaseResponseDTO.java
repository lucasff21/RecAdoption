package com.recsys.recPet.dto.usuario;

import com.recsys.recPet.dto.questionario.QuestionarioResponseDTO;
import com.recsys.recPet.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UsuarioBaseResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private String localidade;
    private String uf;
    private String telefone;
    private LocalDate dataNascimento;
    private QuestionarioResponseDTO questionario;

    public static UsuarioBaseResponseDTO fromEntity(User user) {
        if (user == null) return null;

        UsuarioBaseResponseDTO dto = new UsuarioBaseResponseDTO();
        dto.setId(user.getId());
        dto.setNome(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setLocalidade(user.getEndereco().getLocalidade());
        dto.setTelefone(user.getTelefone());
        dto.setUf(user.getEndereco().getUf());
        dto.setDataNascimento(user.getDataNascimento());
        if (user.getQuestionario() != null) {
            dto.setQuestionario(new QuestionarioResponseDTO(user.getQuestionario()));
        } else {
            dto.setQuestionario(null);
        }
        return dto;
    }
}
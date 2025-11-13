package com.recsys.recPet.dto.usuario;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.dto.questionario.QuestionarioResponseDTO;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.helpers.StringHelper;
import com.recsys.recPet.model.Endereco;
import com.recsys.recPet.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioResponseDTO {
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
    private List<AdocaoResponseDTO> solicitacoes;
    private QuestionarioResponseDTO questionario;

    public UsuarioResponseDTO(User user) {
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
        this.solicitacoes = user.getAdocoes().stream().map(AdocaoResponseDTO::fromEntity).toList();
        if (user.getQuestionario() != null) {
            this.questionario = new QuestionarioResponseDTO(user.getQuestionario());
        } else {
            this.questionario = null;
        }
    }

    @JsonProperty("cpf")
    public String getCpfMasked() {
        return StringHelper.maskCpf(cpf);
    }
}

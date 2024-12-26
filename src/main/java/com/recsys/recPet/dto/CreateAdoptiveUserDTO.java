package com.recsys.recPet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

import com.recsys.recPet.helpers.CPF;

@Getter
@Setter
@AllArgsConstructor
public class CreateAdoptiveUserDTO {
    @NotBlank(message = "Nome é obrigatório")
    public String nome;

    @JsonProperty("cpf")
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
    public String cpf;

    @NotBlank(message = "Gênero é obrigatório")
    public String genero;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @Past(message = "Data de nascimento deve ser no passado")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate dataNascimento;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    public String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    public String senha;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter entre 10 e 11 dígitos")
    public String telefone;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido, deve estar no formato 00000-000 ou 00000000")
    public String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    public String logradouro;

    @NotBlank(message = "Bairro é obrigatório")
    public String bairro;

    @NotBlank(message = "Localidade é obrigatória")
    public String localidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "UF deve conter exatamente 2 caracteres")
    public String uf;

    public String complemento;

    @AssertTrue(message = "Usuário deve ser maior de idade")
    @JsonIgnore
    public boolean isDataNascimento() {
        if (dataNascimento == null) return false;

        return Period.between(dataNascimento, LocalDate.now()).getYears() >= 18;
    }

    @AssertTrue(message = "CPF inválido")
    @JsonIgnore
    public boolean isCpf() {
        if (cpf == null) return false;

        return CPF.validar(cpf);
    }
}

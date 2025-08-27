package com.recsys.recPet.dto.usuario;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EnderecoDTO {
    @Size(min = 8, max = 9, message = "CEP deve ter 8 caracteres")
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
}
package com.recsys.recPet.dto.pagina;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class PaginaResponseDTO {
    private Long id;
    private String nome;
    private String titulo;
    private String conteudo;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
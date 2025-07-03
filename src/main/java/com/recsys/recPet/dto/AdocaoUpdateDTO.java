package com.recsys.recPet.dto;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdocaoUpdateDTO {

    @NotNull(message = "O status da solicitação não pode ser nulo.")
    private AdocaoStatus status;

    @Size(max = 500, message = "A observação não pode exceder 500 caracteres.")
    private String observacoes;
}
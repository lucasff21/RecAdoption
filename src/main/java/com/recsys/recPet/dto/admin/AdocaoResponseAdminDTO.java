package com.recsys.recPet.dto.admin;

import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.dto.usuario.UsuarioBaseResponseDTO;
import com.recsys.recPet.model.Adocao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdocaoResponseAdminDTO extends AdocaoResponseDTO {
    private String observacoes;
    private UsuarioBaseResponseDTO usuario;


    public static AdocaoResponseAdminDTO fromEntity(Adocao adocao) {
        if (adocao == null) {
            return null;
        }

        AdocaoResponseAdminDTO adocaoResponseDTO = new AdocaoResponseAdminDTO();

        adocaoResponseDTO.mapFromEntity(adocao);
        adocaoResponseDTO.setObservacoes(adocao.getObservacoes());
        adocaoResponseDTO.setUsuario(UsuarioBaseResponseDTO.fromEntity(adocao.getUser()));

        return adocaoResponseDTO;
    }
}
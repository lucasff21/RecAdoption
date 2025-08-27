package com.recsys.recPet.dto.animal;

import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.model.Animal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AnimalResponseDTO {
    private Long id;
    private String nome;
    private Tipo tipo;
    private Sexo sexo;
    private Porte porte;
    private Pelagem pelagem;
    private LocalDate dataNascimentoAproximada;
    private String imagemPath;
    private List<CaracteristicaDTO> caracteristicas;
    private String descricao;
    private Boolean disponivelParaAdocao;

    public AnimalResponseDTO() {
        this.caracteristicas = List.of();
    }

    public static AnimalResponseDTO fromEntity(Animal animal) {
        if (animal == null) {
            return null;
        }

        AnimalResponseDTO dto = new AnimalResponseDTO();
        dto.setId(animal.getId());
        dto.setNome(animal.getNome());
        dto.setTipo(animal.getTipo());
        dto.setSexo(animal.getSexo());
        dto.setPorte(animal.getPorte());
        dto.setPelagem(animal.getPelagem());
        dto.setDataNascimentoAproximada(animal.getDataNascimentoAproximada());
        dto.setImagemPath(animal.getImagemPath());
        dto.setDescricao(animal.getDescricao());
        dto.setDisponivelParaAdocao(animal.getDisponivelParaAdocao());

        if (animal.getAnimalCaracteristicas() != null) {
            List<CaracteristicaDTO> caracteristicasDTO = animal.getAnimalCaracteristicas().stream()
                    .map(ac -> CaracteristicaDTO.fromEntity(ac.getCaracteristica()))
                    .collect(Collectors.toList());
            dto.setCaracteristicas(caracteristicasDTO);
        } else {
            dto.setCaracteristicas(List.of());
        }

        return dto;
    }

}
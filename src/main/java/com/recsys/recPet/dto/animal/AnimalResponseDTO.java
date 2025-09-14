package com.recsys.recPet.dto.animal;

import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.model.Animal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
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

    public AnimalResponseDTO(Animal animal) {
        this.id = animal.getId();
        this.nome = animal.getNome();
        this.tipo = animal.getTipo();
        this.sexo = animal.getSexo();
        this.porte = animal.getPorte();
        this.pelagem = animal.getPelagem();
        this.dataNascimentoAproximada = animal.getDataNascimentoAproximada();
        this.imagemPath = animal.getImagemPath();
        this.descricao = animal.getDescricao();
        this.disponivelParaAdocao = animal.getDisponivelParaAdocao();

        if (animal.getAnimalCaracteristicas() != null) {
            this.caracteristicas = animal.getAnimalCaracteristicas().stream()
                    .map(ac -> CaracteristicaDTO.fromEntity(ac.getCaracteristica()))
                    .collect(Collectors.toList());
        } else {
            this.caracteristicas = List.of();
        }
    }

    public static AnimalResponseDTO fromEntity(Animal animal) {
        if (animal == null) {
            return null;
        }
        return new AnimalResponseDTO(animal);
    }
}
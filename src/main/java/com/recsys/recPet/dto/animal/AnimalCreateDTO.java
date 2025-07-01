package com.recsys.recPet.dto.animal;

import com.recsys.recPet.enums.pet.Pelagem;
import com.recsys.recPet.enums.pet.Porte;
import com.recsys.recPet.enums.pet.Sexo;
import com.recsys.recPet.model.Animal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AnimalCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "Data de nascimento aproximada é obrigatória")
    private LocalDate dataNascimentoAproximada;

    @NotNull(message = "Sexo é obrigatório")
    private Sexo sexo;

    @NotNull(message = "Porte é obrigatório")
    private Porte porte;

    @NotNull(message = "Pelagem é obrigatório")
    private Pelagem pelagem;

    private MultipartFile imagem;

    private List<String> caracteristicas;

    public Animal toEntity() {
        Animal animal = new Animal();
        animal.setNome(this.nome);
        animal.setDataNascimentoAproximada(this.dataNascimentoAproximada);
        animal.setSexo(this.sexo.name());
        animal.setPorte(this.porte.name());
        animal.setPelagem(this.pelagem.name());

        return animal;
    }
}
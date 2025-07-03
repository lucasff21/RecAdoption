package com.recsys.recPet.dto.animal;

import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.model.Animal;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;
        import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List; // Novo
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class AnimalUpdateDTO {
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private LocalDate dataNascimentoAproximada;

    private Sexo sexo;

    private Porte porte;

    private Pelagem pelagem;

    private List<String> caracteristicas;

    private MultipartFile novaImagem;

    public void updateEntity(Animal animal) {
        Optional.ofNullable(this.nome).ifPresent(animal::setNome);
        Optional.ofNullable(this.dataNascimentoAproximada).ifPresent(animal::setDataNascimentoAproximada);
        Optional.ofNullable(this.sexo).ifPresent(sexo -> animal.setSexo(getSexo()));
        Optional.ofNullable(this.porte).ifPresent(porte -> animal.setPorte(getPorte()));
        Optional.ofNullable(this.pelagem).ifPresent(pelagem -> animal.setPelagem(getPelagem()));
    }
}
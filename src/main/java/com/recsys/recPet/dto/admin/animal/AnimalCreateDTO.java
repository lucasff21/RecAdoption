package com.recsys.recPet.dto.admin.animal;

import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
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

    @NotNull(message = "Tipo é obrigatório")
    private Tipo tipo;

    @NotNull(message = "Sexo é obrigatório")
    private Sexo sexo;

    @NotNull(message = "Porte é obrigatório")
    private Porte porte;

    @NotNull(message = "Pelagem é obrigatório")
    private Pelagem pelagem;

    private MultipartFile imagem;

    private List<String> caracteristicas;

    private String descricao;

    private List<Long> caracteristicasIds;

    private boolean disponivelParaAdocao;

    public Animal toEntity() {
        Animal animal = new Animal();
        animal.setNome(this.nome);
        animal.setDataNascimentoAproximada(this.dataNascimentoAproximada);
        animal.setSexo(this.sexo);
        animal.setPorte(this.porte);
        animal.setPelagem(this.pelagem);
        animal.setTipo(this.tipo);
        animal.setDescricao(this.descricao);
        animal.setDisponivelParaAdocao(this.disponivelParaAdocao);
        return animal;
    }
}
package com.recsys.recPet.dto.admin.animal;

import com.recsys.recPet.enums.animal.Pelagem;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.model.Animal;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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

    private boolean castrado;
    private LocalDate dataUltimaVermifugacao;
    private LocalDate dataUltimaVacinaAntirrabica;
    private LocalDate dataUltimaVacinaMultipla;
    @Size(max = 10, message = "Tipo da vacina deve ter no máximo 20 caracteres")
    private String tipoVacinaMultipla;

    @Size(max = 10)
    private String rgAnimal;

    @Size(max = 20)
    private String microchipId;

    @Size(max = 20)
    private String raca;

    private String observacoesPrivadas;

    private String observacoesMedicas;

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

        animal.setCastrado(this.castrado);
        animal.setDataUltimaVermifugacao(this.dataUltimaVermifugacao);
        animal.setDataUltimaVacinaAntirrabica(this.dataUltimaVacinaAntirrabica);
        animal.setDataUltimaVacinaMultipla(this.dataUltimaVacinaMultipla);
        animal.setTipoVacinaMultipla(this.tipoVacinaMultipla);
        animal.setObservacoesMedicas(this.observacoesMedicas);
        animal.setRgAnimal(this.rgAnimal);
        animal.setObservacoesPrivadas(this.observacoesPrivadas);
        animal.setMicrochipId(this.microchipId);
        animal.setRaca(this.raca);

        return animal;
    }
}
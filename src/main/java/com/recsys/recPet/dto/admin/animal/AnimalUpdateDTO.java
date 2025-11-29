package com.recsys.recPet.dto.admin.animal;

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
import java.util.List;
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

    private List<Long> caracteristicasIds;

    private MultipartFile novaImagem;

    private String descricao;

    private Boolean disponivelParaAdocao;

    private Boolean castrado;
    private LocalDate dataUltimaVermifugacao;
    private LocalDate dataUltimaVacinaAntirrabica;
    private LocalDate dataUltimaVacinaMultipla;
    private String tipoVacinaMultipla;
    private String observacoesMedicas;
    @Size(max = 10)
    private String rgAnimal;

    @Size(max = 20)
    private String microchipId;

    @Size(max = 20)
    private String raca;

    private String observacoesPrivadas;

    public void updateEntity(Animal animal) {
        Optional.ofNullable(this.nome).ifPresent(animal::setNome);
        Optional.ofNullable(this.dataNascimentoAproximada).ifPresent(animal::setDataNascimentoAproximada);
        Optional.ofNullable(this.sexo).ifPresent(animal::setSexo);
        Optional.ofNullable(this.porte).ifPresent(animal::setPorte);
        Optional.ofNullable(this.pelagem).ifPresent(animal::setPelagem);
        Optional.ofNullable(this.descricao).ifPresent(animal::setDescricao);
        Optional.ofNullable(this.disponivelParaAdocao).ifPresent(animal::setDisponivelParaAdocao);

        Optional.ofNullable(this.castrado).ifPresent(animal::setCastrado);
        animal.setDataUltimaVermifugacao(this.dataUltimaVermifugacao);
        animal.setDataUltimaVacinaAntirrabica(this.dataUltimaVacinaAntirrabica);
        animal.setDataUltimaVacinaMultipla(this.dataUltimaVacinaMultipla);
        animal.setTipoVacinaMultipla(this.tipoVacinaMultipla);
        animal.setObservacoesMedicas(this.observacoesMedicas);
        animal.setRgAnimal(this.rgAnimal);
        animal.setObservacoesPrivadas(this.observacoesPrivadas);
        animal.setMicrochipId(this.microchipId);
        animal.setRaca(this.raca);
    }
}
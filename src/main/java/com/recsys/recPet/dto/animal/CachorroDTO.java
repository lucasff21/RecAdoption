package com.recsys.recPet.dto.animal;

import com.recsys.recPet.enums.pet.Pelagem;
import com.recsys.recPet.enums.pet.Porte;
import com.recsys.recPet.enums.pet.Sexo;
import com.recsys.recPet.model.Cachorro;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class CachorroDTO {
    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Data de nascimento não pode ser vazia")
    private LocalDate dataNascimentoAproximada;

    @NotNull(message = "Sexo não pode ser nulo")
    private Sexo sexo;

    @NotNull(message = "Porte não pode ser nulo")
    private Porte porte;

    @NotNull(message = "Pelagem não pode ser nula")
    private Pelagem pelagem;

    @NotNull(message = "Campo 'ideal para casa' não pode ser nulo")
    private Boolean idealCasa;

    @NotNull(message = "Campo 'gosta de crianças' não pode ser nulo")
    private Boolean gostaCrianca;

    @NotNull(message = "Campo 'cão de guarda' não pode ser nulo")
    private Boolean caoGuarda;

    @NotNull(message = "Campo 'brincalhão' não pode ser nulo")
    private Boolean brincalhao;

    @NotNull(message = "Campo 'necessidade de correr' não pode ser nulo")
    private Boolean necessidadeCorrer;

    @NotNull(message = "Campo 'queda de pelo' não pode ser nulo")
    private Boolean quedaPelo;

    @NotNull(message = "Campo 'tende a latir' não pode ser nulo")
    private Boolean tendeLatir;

    private MultipartFile imagem;

    public Cachorro toEntity() {
        Cachorro cachorro = new Cachorro();
        cachorro.setNome(this.nome);
        cachorro.setDataNascimentoAproximada(this.dataNascimentoAproximada);
        cachorro.setSexo(this.sexo.name());
        cachorro.setPorte(this.porte.name());
        cachorro.setPelagem(this.pelagem.name());
        cachorro.setIdealCasa(this.idealCasa);
        cachorro.setGostaCrianca(this.gostaCrianca);
        cachorro.setCaoGuarda(this.caoGuarda);
        cachorro.setBrincalhao(this.brincalhao);
        cachorro.setNecessidadeCorrer(this.necessidadeCorrer);
        cachorro.setQuedaPelo(this.quedaPelo);
        cachorro.setTendeLatir(this.tendeLatir);
        BeanUtils.copyProperties(this, cachorro);
        return cachorro;
    }
}
package com.recsys.recPet.dto.cachorro;

import com.recsys.recPet.enums.pet.Pelagem;
import com.recsys.recPet.enums.pet.Porte;
import com.recsys.recPet.enums.pet.Sexo;
import com.recsys.recPet.model.Cachorro;
import lombok.AllArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class CachorroUpdateDTO {
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String idade;

    private Sexo sexo;

    private Porte porte;

    private Pelagem pelagem;

    private Boolean idealCasa;

    private Boolean gostaCrianca;

    private Boolean caoGuarda;

    private Boolean brincalhao;

    private Boolean necessidadeCorrer;

    private Boolean quedaPelo;

    private Boolean tendeLatir;

    private MultipartFile novaImagem;

    public void updateEntity(Cachorro cachorro) {
        Optional.ofNullable(this.nome).ifPresent(cachorro::setNome);
        Optional.ofNullable(this.idade).ifPresent(cachorro::setIdade);
        Optional.ofNullable(this.sexo).ifPresent(sexo -> cachorro.setSexo(sexo.name()));
        Optional.ofNullable(this.porte).ifPresent(porte -> cachorro.setPorte(porte.name()));
        Optional.ofNullable(this.pelagem).ifPresent(pelagem -> cachorro.setPelagem(pelagem.name()));
        Optional.ofNullable(this.idealCasa).ifPresent(cachorro::setIdealCasa);
        Optional.ofNullable(this.gostaCrianca).ifPresent(cachorro::setGostaCrianca);
        Optional.ofNullable(this.caoGuarda).ifPresent(cachorro::setCaoGuarda);
        Optional.ofNullable(this.brincalhao).ifPresent(cachorro::setBrincalhao);
        Optional.ofNullable(this.necessidadeCorrer).ifPresent(cachorro::setNecessidadeCorrer);
        Optional.ofNullable(this.quedaPelo).ifPresent(cachorro::setQuedaPelo);
        Optional.ofNullable(this.tendeLatir).ifPresent(cachorro::setTendeLatir);
    }
}
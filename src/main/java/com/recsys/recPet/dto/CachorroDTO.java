package com.recsys.recPet.dto;

import com.recsys.recPet.enums.pet.Pelagem;
import com.recsys.recPet.enums.pet.Porte;
import com.recsys.recPet.enums.pet.Sexo;
import com.recsys.recPet.model.Cachorro;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class CachorroDTO {
    @NotBlank(message = "Nome não pode ser vazio")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Idade não pode ser vazia")
    private String idade;

    @NotNull(message = "Sexo não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotNull(message = "Porte não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private Porte porte;

    @NotNull(message = "Pelagem não pode ser nula")
    @Enumerated(EnumType.STRING)
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
        cachorro.setIdade(this.idade);
        cachorro.setSexo(this.sexo.toString());
        cachorro.setPorte(this.porte.toString());
        cachorro.setPelagem(this.pelagem.toString());
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
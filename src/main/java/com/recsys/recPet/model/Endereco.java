package com.recsys.recPet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "enderecos")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;


    @Override
    public String toString() {
        return "Endereco [cep=" + cep + ", logradouro=" + logradouro + ", complemento=" + complemento
                + ", bairro=" + bairro + ", localidade=" + localidade + ", uf=" + uf + "]";
    }

}

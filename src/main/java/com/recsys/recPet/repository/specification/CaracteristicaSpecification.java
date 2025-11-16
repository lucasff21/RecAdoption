package com.recsys.recPet.repository.specification;
import com.recsys.recPet.model.Caracteristica;
import org.springframework.data.jpa.domain.Specification;

public class CaracteristicaSpecification {

    public static Specification<Caracteristica> comNome(String nome) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + nome.toLowerCase() + "%"
                );
    }

    public static Specification<Caracteristica> comAtivo(Boolean ativo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("ativo"), ativo);
    }
}

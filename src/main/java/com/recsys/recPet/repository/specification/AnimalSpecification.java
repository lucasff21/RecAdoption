package com.recsys.recPet.repository.specification;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.AnimalCaracteristica;
import com.recsys.recPet.model.Caracteristica;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class AnimalSpecification {

    public static Specification<Animal> hasSexo(String sexo) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("sexo"), sexo);
    }

    public static Specification<Animal> hasPorte(String porte) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("porte"), porte);
    }


    public static Specification<Animal> hasCaracteristicas(List<String> caracteristicas) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);

            Join<Animal, AnimalCaracteristica> animalCaracteristicaJoin = root.join("animalCaracteristicas", JoinType.INNER);
            Join<AnimalCaracteristica, Caracteristica> caracteristicaJoin = animalCaracteristicaJoin.join("caracteristica", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();
            for (String caracteristicaNome : caracteristicas) {
                predicates.add(criteriaBuilder.equal(caracteristicaJoin.get("nome"), caracteristicaNome));
            }

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}

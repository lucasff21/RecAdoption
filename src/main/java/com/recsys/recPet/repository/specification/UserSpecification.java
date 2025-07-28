package com.recsys.recPet.repository.specification;

import com.recsys.recPet.model.User;
import com.recsys.recPet.enums.filtro.TipoBusca;
import com.recsys.recPet.enums.TipoUsuario;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {
    public static Specification<User> filterBy(String valor, TipoBusca tipoBusca, TipoUsuario role) {
        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();


            if (valor != null && !valor.trim().isEmpty() && tipoBusca != null) {
                String likePattern = "%" + valor.toLowerCase() + "%";

                if (tipoBusca == TipoBusca.NOME) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), likePattern));
                } else if (tipoBusca == TipoBusca.EMAIL) {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern));
                }
            }

            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("tipo"), role));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

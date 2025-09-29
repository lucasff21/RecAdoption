package com.recsys.recPet.repository.specification;

import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class AdocaoSpecification {

    public static Specification<Adocao> comStatus(AdocaoStatus status) {
        return (root, query, builder) -> {
            if (status == null) {
                return builder.conjunction();
            }
            return builder.equal(root.get("status"), status);
        };
    }

    public static Specification<Adocao> comTermoDeBusca(String termo) {
        return (root, query, builder) -> {
            if (!StringUtils.hasText(termo)) {
                return builder.conjunction();
            }

            String likeTerm = "%" + termo.toLowerCase() + "%";

            Join<Object, Object> adotanteJoin = root.join("user");
            Join<Object, Object> petJoin = root.join("animal");

            return builder.or(
                    builder.like(builder.lower(adotanteJoin.get("nome")), likeTerm),
                    builder.like(builder.lower(adotanteJoin.get("email")), likeTerm),
                    builder.like(builder.lower(petJoin.get("nome")), likeTerm)
            );
        };
    }
}
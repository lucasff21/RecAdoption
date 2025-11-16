package com.recsys.recPet.repository.specification;
import com.recsys.recPet.enums.animal.Porte;
import com.recsys.recPet.enums.animal.Sexo;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.AnimalCaracteristica;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AnimalSpecification {
    public static Specification<Animal> comNome(String nome) {
        return (root, query, criteriaBuilder) -> {
            if (nome == null || nome.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<Animal> comSexo(String sexoFiltro) {
        return (root, query, criteriaBuilder) -> {
            if (sexoFiltro == null || sexoFiltro.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Sexo sexoEnum = Sexo.fromString(sexoFiltro);
            if (sexoEnum == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("sexo"), sexoEnum);
        };
    }

    public static Specification<Animal> comPorte(String porteFiltro) {
        return (root, query, criteriaBuilder) -> {
            if (porteFiltro == null || porteFiltro.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Porte porteEnum = Porte.fromString(porteFiltro);
            if (porteEnum == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("porte"), porteEnum);
        };
    }

    public static Specification<Animal> comFaixaEtaria(String faixaEtaria) {
        return (root, query, criteriaBuilder) -> {
            if (faixaEtaria == null || faixaEtaria.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            LocalDate today = LocalDate.now();
            LocalDate minDate = null;
            LocalDate maxDate = null;

            switch (faixaEtaria.toLowerCase()) {
                case "filhote": // 0-1 ano
                    minDate = today.minusYears(1);
                    maxDate = today;
                    break;
                case "adolescente": // 1-3 anos
                    minDate = today.minusYears(3);
                    maxDate = today.minusYears(1).minusDays(1);
                    break;
                case "adulto": // 3-8 anos
                    minDate = today.minusYears(8);
                    maxDate = today.minusYears(3).minusDays(1);
                    break;
                case "idoso": // +8 anos
                    maxDate = today.minusYears(8).minusDays(1);
                    break;
                default:
                    return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();
            if (minDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataNascimentoAproximada"), minDate));
            }
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataNascimentoAproximada"), maxDate));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Animal> comCaracteristicasPorId(List<Long> caracteristicaIds) {
        return (root, query, criteriaBuilder) -> {

            if (caracteristicaIds == null || caracteristicaIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicados = new ArrayList<>();

            for (Long caracteristicaId : caracteristicaIds) {

                Subquery<Integer> subquery = query.subquery(Integer.class);
                subquery.select(criteriaBuilder.literal(1));
                Root<AnimalCaracteristica> subqueryACRoot = subquery.from(AnimalCaracteristica.class);

                subquery.where(
                        criteriaBuilder.equal(
                                subqueryACRoot.get("animal"),
                                root
                        ),
                        criteriaBuilder.equal(
                                subqueryACRoot.get("caracteristica").get("id"),
                                caracteristicaId
                        )
                );

                predicados.add(criteriaBuilder.exists(subquery));
            }
            return criteriaBuilder.and(predicados.toArray(new Predicate[0]));
        };
    }

    public static Specification<Animal> disponivelParaAdocao(Boolean disponivel) {
        return (root, query, criteriaBuilder) -> {
            if (disponivel == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("disponivelParaAdocao"), disponivel);
        };
    }

    public static Specification<Animal> comTipo(Tipo tipo) {
        return (root, query, criteriaBuilder) -> {
            if (tipo == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("tipo"), tipo);
        };
    }

    public static Specification<Animal> comCastrado(Boolean castrado) {
        return (root, query, criteriaBuilder) -> {
            if (castrado == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("castrado"), castrado);
        };
    }

    public static Specification<Animal> comVermifugado(Boolean vermifugado) {
        return (root, query, criteriaBuilder) -> {
            if (vermifugado == null) {
                return criteriaBuilder.conjunction();
            }
            if (vermifugado) {
                return criteriaBuilder.isNotNull(root.get("dataUltimaVermifugacao"));
            } else {
                return criteriaBuilder.isNull(root.get("dataUltimaVermifugacao"));
            }
        };
    }

    public static Specification<Animal> comVacinado(Boolean vacinado) {
        return (root, query, criteriaBuilder) -> {
            if (vacinado == null) {
                return criteriaBuilder.conjunction();
            }

            Predicate antirrabicaNotNull = criteriaBuilder.isNotNull(root.get("dataUltimaVacinaAntirrabica"));
            Predicate multiplaNotNull = criteriaBuilder.isNotNull(root.get("dataUltimaVacinaMultipla"));

            if (vacinado) {
                return criteriaBuilder.and(antirrabicaNotNull, multiplaNotNull);
            } else {
                Predicate antirrabicaNull = criteriaBuilder.isNull(root.get("dataUltimaVacinaAntirrabica"));
                Predicate multiplaNull = criteriaBuilder.isNull(root.get("dataUltimaVacinaMultipla"));
                return criteriaBuilder.or(antirrabicaNull, multiplaNull);
            }
        };
    }
}

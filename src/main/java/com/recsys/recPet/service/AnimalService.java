package com.recsys.recPet.service;

import com.recsys.recPet.dto.animal.AnimalUpdateDTO;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.AnimalCaracteristicaId;
import com.recsys.recPet.model.Caracteristica;
import com.recsys.recPet.model.AnimalCaracteristica;
import com.recsys.recPet.repository.AnimalRepository;
import com.recsys.recPet.repository.CaracteristicaRepository;
import com.recsys.recPet.repository.AnimalCaracteristicaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ImageService imageService;
    private final CaracteristicaRepository caracteristicaRepository;
    private final AnimalCaracteristicaRepository animalCaracteristicaRepository; // Novo

    public AnimalService(AnimalRepository animalRepository, ImageService imageService,
                         CaracteristicaRepository caracteristicaRepository,
                         AnimalCaracteristicaRepository animalCaracteristicaRepository) {
        this.animalRepository = animalRepository;
        this.imageService = imageService;
        this.caracteristicaRepository = caracteristicaRepository;
        this.animalCaracteristicaRepository = animalCaracteristicaRepository;
    }

    @Transactional(readOnly = true)
    public Page<Animal> findAll(
            String nome,
            String sexo,
            String porte,
            List<String> caracteristicaNomes,
            String faixaEtaria,
            Pageable pageable
    ) {
        Specification<Animal> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }

            if (sexo != null && !sexo.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("sexo"), sexo));
            }
            if (porte != null && !porte.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("porte"), porte));
            }

            if (faixaEtaria != null && !faixaEtaria.isEmpty()) {
                LocalDate today = LocalDate.now();
                LocalDate minDate = null;
                LocalDate maxDate = null;

                switch (faixaEtaria.toLowerCase()) {
                    case "filhote":
                        maxDate = today;
                        minDate = today.minusYears(1);
                        break;
                    case "adolescente":
                        maxDate = today.minusYears(1).minusDays(1);
                        minDate = today.minusYears(3);
                        break;
                    case "adulto":
                        maxDate = today.minusYears(3).minusDays(1);
                        minDate = today.minusYears(8);
                        break;
                    case "idoso":
                        maxDate = today.minusYears(8).minusDays(1);
                        break;
                    default:
                        break;
                }

                if (minDate != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataNascimentoAproximada"), minDate));
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataNascimentoAproximada"), maxDate));
                }


                if (faixaEtaria.equalsIgnoreCase("idoso")) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataNascimentoAproximada"), maxDate));
                }
            }

            if (caracteristicaNomes != null && !caracteristicaNomes.isEmpty()) {
                Join<Object, Object> animalCaracteristicasJoin = root.join("animalCaracteristicas");
                Join<AnimalCaracteristica, Caracteristica> caracteristicaJoin = animalCaracteristicasJoin.join("caracteristica");
                predicates.add(caracteristicaJoin.get("nome").in(caracteristicaNomes));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return animalRepository.findAll(spec, pageable);
    }

    public Animal findById(Long id) {
        return animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));
    }

    @Transactional
    public Animal save(Animal animal) {
        return animalRepository.save(animal);
    }

    @Transactional
    public Animal update(@NotNull AnimalUpdateDTO animalDTO, Long id) throws IOException {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com o ID fornecido."));

        animalDTO.updateEntity(animal);

        if (animalDTO.getCaracteristicas() != null) {
            Set<AnimalCaracteristica> novasAssociacoesParaAdicionar = new HashSet<>();
            List<Caracteristica> caracteristicasExistentes = caracteristicaRepository
                    .findByNomeIn(animalDTO.getCaracteristicas());

            Set<String> nomesCaracteristicasAtuais = animal.getAnimalCaracteristicas().stream()
                    .map(ac -> ac.getCaracteristica().getNome())
                    .collect(Collectors.toSet());

            for (Caracteristica carac : caracteristicasExistentes) {
                if (!nomesCaracteristicasAtuais.contains(carac.getNome())) {
                    AnimalCaracteristica novaAssociacao = new AnimalCaracteristica();
                    novaAssociacao.setAnimal(animal);
                    novaAssociacao.setCaracteristica(carac);
                    novaAssociacao.setCreatedAt(LocalDateTime.now());
                    if (animal.getId() != null && carac.getId() != null) {
                        novaAssociacao.setId(new AnimalCaracteristicaId(animal.getId(), carac.getId()));
                    }
                    novasAssociacoesParaAdicionar.add(novaAssociacao);
                }
            }

            Set<AnimalCaracteristica> paraRemover = animal.getAnimalCaracteristicas().stream()
                    .filter(ac -> !animalDTO.getCaracteristicas().contains(ac.getCaracteristica().getNome()))
                    .collect(Collectors.toSet());

            paraRemover.forEach(animal::removeAnimalCaracteristica);
            animalCaracteristicaRepository.deleteAll(paraRemover);

            novasAssociacoesParaAdicionar.forEach(animal::addAnimalCaracteristica);
        }

        MultipartFile imagemFile = animalDTO.getNovaImagem();
        if (imagemFile != null && !imagemFile.isEmpty()) {
            if (animal.getImagemPath() != null && !animal.getImagemPath().isEmpty()) {
                try {
                    String oldFileName = imageService.extrairPublicIdCloudinaryUrl(animal.getImagemPath());
                    if (oldFileName != null) {
                        imageService.delete(oldFileName);
                    }
                } catch (Exception e) {
                    throw new IOException("Erro ao excluir a imagem antiga: " + e.getMessage(), e);
                }
            }
            Map<?, ?> image = this.imageService.uploadImage(imagemFile, "pets/adoption");
            animal.setImagemPath((String) image.get("secure_url"));
        }

        return animalRepository.save(animal);
    }

    @Transactional
    public void delete(Long id) {
        Animal animal = findById(id);
        animalRepository.delete(animal);
    }
}
package com.recsys.recPet.service;

import com.recsys.recPet.dto.animal.AnimalCreateDTO;
import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.dto.animal.AnimalUpdateDTO;
import com.recsys.recPet.dto.animal.CaracteristicaDTO;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.AnimalCaracteristicaId;
import com.recsys.recPet.model.Caracteristica;
import com.recsys.recPet.model.AnimalCaracteristica;
import com.recsys.recPet.repository.AnimalRepository;
import com.recsys.recPet.repository.CaracteristicaRepository;
import com.recsys.recPet.repository.AnimalCaracteristicaRepository;
import com.recsys.recPet.repository.specification.AnimalSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageImpl;

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
    public PageImpl<AnimalResponseDTO> findAll(
            String nome,
            String sexo,
            String porte,
            List<Long> caracteristicaIds,
            String faixaEtaria,
            Pageable pageable
    ) {
        Specification<Animal> spec = Specification
                .where(AnimalSpecification.comNome(nome))
                .and(AnimalSpecification.comSexo(sexo))
                .and(AnimalSpecification.comPorte(porte))
                .and(AnimalSpecification.comFaixaEtaria(faixaEtaria))
                .and(AnimalSpecification.comCaracteristicasPorId(caracteristicaIds));

        Page<Animal> animalPage = animalRepository.findAll(spec, pageable);

        List<AnimalResponseDTO> dtoList = animalPage.getContent().stream()
                .map(AnimalResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, animalPage.getTotalElements());
    }

    public Animal findById(Long id) throws Exception {
        return animalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado com o ID fornecido."));
    }

    @Transactional
    public Animal atualizarAnimal(Long idAnimal, AnimalUpdateDTO animalDTO) throws IOException {
        Animal animalExistente = animalRepository.findById(idAnimal)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado com o ID: " + idAnimal));

        animalDTO.updateEntity(animalExistente);

        List< Long> novosIdsCaracteristicas = animalDTO.getCaracteristicasIds();

        if (novosIdsCaracteristicas == null || novosIdsCaracteristicas.isEmpty()) {
            animalCaracteristicaRepository.deleteAll(animalExistente.getAnimalCaracteristicas());
            animalExistente.getAnimalCaracteristicas().clear();
        } else {
            this.adicionarRemoverCaracteristicas(animalExistente, novosIdsCaracteristicas);
        }

        if (animalDTO.getNovaImagem() != null) {
            Map<?, ?> image = this.imageService.uploadImage(animalDTO.getNovaImagem(), "pets/adoption");

            if (!animalExistente.getImagemPath().isEmpty()) {
                imageService.delete(animalExistente.getImagemPath());
            }

            imageService.delete(animalExistente.getImagemPath());

            animalExistente.setImagemPath((String) image.get("secure_url"));
        }

        return animalRepository.save(animalExistente);
    }

    @Transactional
    protected void adicionarRemoverCaracteristicas(Animal animal, List<Long> novosIdsCaracteristicas) {
        Set<AnimalCaracteristica> associacoesAtuais = new HashSet<>(animal.getAnimalCaracteristicas());
        Set<Long> idsAtuais = associacoesAtuais.stream()
                .map(ac -> ac.getCaracteristica().getId())
                .collect(Collectors.toSet());

        Set<Long> novosIdsSet = new HashSet<>(novosIdsCaracteristicas);

        Set<AnimalCaracteristica> paraRemover = associacoesAtuais.stream()
                .filter(ac -> !novosIdsSet.contains(ac.getCaracteristica().getId()))
                .collect(Collectors.toSet());

        if (!paraRemover.isEmpty()) {
            paraRemover.forEach(animal::removeAnimalCaracteristica);
            animalCaracteristicaRepository.deleteAll(paraRemover);
        }

        Set<Long> idsParaAdicionar = new HashSet<>(novosIdsSet);
        idsParaAdicionar.removeAll(idsAtuais);

        if (!idsParaAdicionar.isEmpty()) {
            List<Caracteristica> caracteristicasParaAdicionar = caracteristicaRepository.findAllById(idsParaAdicionar);

            for (Caracteristica caracteristica : caracteristicasParaAdicionar) {

                AnimalCaracteristicaId acId = new AnimalCaracteristicaId();
                acId.setAnimalId(animal.getId());
                acId.setCaracteristicaId(caracteristica.getId());

                AnimalCaracteristica novaAssociacao = new AnimalCaracteristica();
                novaAssociacao.setId(acId);
                novaAssociacao.setCaracteristica(caracteristica);
                animal.addAnimalCaracteristica(novaAssociacao);
            }
        }
    }

    @Transactional
    public void criarAnimal(AnimalCreateDTO animalDTO) throws IOException {
        Animal animal = animalDTO.toEntity();

        if (animalDTO.getImagem() != null && !animalDTO.getImagem().isEmpty()) {
            Map<?, ?> image = this.imageService.uploadImage(animalDTO.getImagem(), "pets/adoption");
            animal.setImagemPath((String) image.get("secure_url"));
        }

        Animal savedAnimal = animalRepository.save(animal);

        if (animalDTO.getCaracteristicasIds() != null && !animalDTO.getCaracteristicasIds().isEmpty()) {
            List<Caracteristica> caracteristicas = caracteristicaRepository.findAllById(animalDTO.getCaracteristicasIds());
            for (Caracteristica carac : caracteristicas) {
                AnimalCaracteristica ac = new AnimalCaracteristica();
                ac.setAnimal(savedAnimal);
                ac.setCaracteristica(carac);
                ac.setCreatedAt(LocalDateTime.now());


                if (savedAnimal.getId() != null && carac.getId() != null) {
                    ac.setId(new AnimalCaracteristicaId(savedAnimal.getId(), carac.getId()));
                }
                savedAnimal.addAnimalCaracteristica(ac);
            }

        }
    }


    @Transactional
    public void delete(Long id) {
        Optional<Animal> animal =  animalRepository.findById(id);
        animal.ifPresent(animalRepository::delete);
    }

    public List<CaracteristicaDTO> findAllCaracteristicas() {
        List<Caracteristica> caracteristicas = caracteristicaRepository.findAll();
        return caracteristicas.stream()
                .map(CaracteristicaDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
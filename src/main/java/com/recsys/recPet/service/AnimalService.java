package com.recsys.recPet.service;

import com.recsys.recPet.dto.admin.animal.AnimalAdminResponseDTO;
import com.recsys.recPet.dto.admin.animal.AnimalCreateDTO;
import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.dto.admin.animal.AnimalUpdateDTO;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.model.*;
import com.recsys.recPet.repository.*;
import com.recsys.recPet.repository.specification.AnimalSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final ImageService imageService;
    private final CaracteristicaRepository caracteristicaRepository;
    private final AnimalCaracteristicaRepository animalCaracteristicaRepository;
    private final CorRepository corRepository;
    private final RacaRepository racaRepository;

    public AnimalService(AnimalRepository animalRepository, ImageService imageService,
                         CaracteristicaRepository caracteristicaRepository,
                         AnimalCaracteristicaRepository animalCaracteristicaRepository, CorRepository corRepository, RacaRepository racaRepository) {
        this.animalRepository = animalRepository;
        this.imageService = imageService;
        this.caracteristicaRepository = caracteristicaRepository;
        this.animalCaracteristicaRepository = animalCaracteristicaRepository;
        this.corRepository = corRepository;
        this.racaRepository = racaRepository;
    }

    @Transactional(readOnly = true)
    public PageImpl<AnimalResponseDTO> findAll(
            String nome,
            String sexo,
            List<String> porte,
            List<Long> caracteristicaIds,
            List<String> faixaEtaria,
            Tipo tipo,
            Boolean castrado,
            Boolean vacinado,
            String microchip,
            List<Long> corIds,
            List<Long> racaIds,
            Pageable pageable
    ) {
        Specification<Animal> spec = Specification
                .where(AnimalSpecification.comNome(nome))
                .and(AnimalSpecification.disponivelParaAdocao(true))
                .and(AnimalSpecification.comSexo(sexo))
                .and(AnimalSpecification.comPortes(porte))
                .and(AnimalSpecification.comFaixasEtarias(faixaEtaria))
                .and(AnimalSpecification.comTipo(tipo))
                .and(AnimalSpecification.comCaracteristicasPorId(caracteristicaIds))
                .and(AnimalSpecification.comCastrado(castrado))
                .and(AnimalSpecification.comMicrochip(microchip))
                .and(AnimalSpecification.comVacinado(vacinado))
                .and(AnimalSpecification.comCores(corIds))
                .and(AnimalSpecification.comRacas(racaIds));

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

        if (animalDTO.getCorId() != null) {
            Cor cor = corRepository.findById(animalDTO.getCorId())
                    .orElseThrow(() -> new RuntimeException("Cor não encontrada"));
            animalExistente.setCor(cor);
        }

        if (animalDTO.getRacaId() != null) {
            Raca novaRaca = racaRepository.findById(animalDTO.getRacaId())
                    .orElseThrow(() -> new RuntimeException("Raça não encontrada"));
            animalExistente.setRaca(novaRaca);
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

        if (animalDTO.getCorId() != null) {
            Cor cor = corRepository.findById(animalDTO.getCorId())
                    .orElseThrow(() -> new RuntimeException("Cor não encontrada"));
            animal.setCor(cor);
        }

        if (animalDTO.getRacaId() != null) {
            Raca novaRaca = racaRepository.findById(animalDTO.getRacaId())
                    .orElseThrow(() -> new RuntimeException("Raça não encontrada"));
            animal.setRaca(novaRaca);
        }
    }

    @Transactional
    public void delete(Long id) {
        Optional<Animal> animal =  animalRepository.findById(id);
        animal.ifPresent(animalRepository::delete);
    }

    @Transactional(readOnly = true)
    public PageImpl<AnimalAdminResponseDTO> findAllForAdmin(
            String nome,
            String sexo,
            List<String> porte,
            List<Long> caracteristicaIds,
            List<String> faixaEtaria,
            List<Long> racaIds,
            List<Long> corIds,
            Boolean disponivelParaAdocao,
            Tipo especie,
            Boolean castrado,
            Boolean vacinado,
            Boolean vermifugado,
            String microchip,
            String rg,
            Pageable pageable
    ) {
        Specification<Animal> spec = Specification
                .where(AnimalSpecification.comNome(nome))
                .and(AnimalSpecification.disponivelParaAdocao(disponivelParaAdocao))
                .and(AnimalSpecification.comSexo(sexo))
                .and(AnimalSpecification.comPortes(porte))
                .and(AnimalSpecification.comFaixasEtarias(faixaEtaria))
                .and(AnimalSpecification.comRacas(racaIds))
                .and(AnimalSpecification.comCores(corIds))
                .and(AnimalSpecification.comTipo(especie))
                .and(AnimalSpecification.comCaracteristicasPorId(caracteristicaIds))
                .and(AnimalSpecification.comCastrado(castrado))
                .and(AnimalSpecification.comVacinado(vacinado))
                .and(AnimalSpecification.comMicrochip(microchip))
                .and(AnimalSpecification.comRg(rg))
                .and(AnimalSpecification.comVermifugado(vermifugado));

        Page<Animal> animalPage = animalRepository.findAll(spec, pageable);

        List<AnimalAdminResponseDTO> dtoList = animalPage.getContent().stream()
                .map(AnimalAdminResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, animalPage.getTotalElements());
    }

    @Transactional
    public void alterarDisponibilidade(Long animalId, Boolean disponivel) {
        Optional<Animal> animal = animalRepository.findById(animalId) ;

        animal.ifPresent(value -> value.setDisponivelParaAdocao(disponivel));
    }
}
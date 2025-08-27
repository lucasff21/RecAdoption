package com.recsys.recPet.controller;

import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.dto.animal.AnimalUpdateDTO;
import com.recsys.recPet.dto.animal.AnimalCreateDTO;
import com.recsys.recPet.dto.animal.CaracteristicaDTO;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.service.AnimalService;
import com.recsys.recPet.service.ImageService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cachorro")
public class AnimalController {

    private final AnimalService animalService;
    private final ImageService imageService;

    Logger logger = org.slf4j.LoggerFactory.getLogger(AnimalController.class);

    private static final int DEFAULT_PAGE_SIZE = 20;

    public AnimalController(AnimalService animalService, ImageService imageService) {
        this.animalService = animalService;
        this.imageService = imageService;
    }

    @GetMapping("/findall")
    public ResponseEntity<Page<AnimalResponseDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) String porte,
            @RequestParam(required = false) List<Long> caracteristicas,
            @RequestParam(required = false) String faixaEtaria,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<AnimalResponseDTO> animalPage = animalService.findAll(
                nome,
                sexo,
                porte,
                caracteristicas,
                faixaEtaria,
                pageable
        );
        return ResponseEntity.status(HttpStatus.OK).body(animalPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> findById(@PathVariable Long id) {
        try {
            Animal animal = animalService.findById(id);
            return ResponseEntity.status(HttpStatus.OK).body(AnimalResponseDTO.fromEntity(animal)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Animal> save(@Valid @ModelAttribute AnimalCreateDTO animalDTO) throws IOException {
       animalService.criarAnimal(animalDTO);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Animal> atualizarAnimal(@PathVariable Long id, @Valid @ModelAttribute AnimalUpdateDTO animalDTO) throws IOException {
        Animal animalAtualizado = animalService.atualizarAnimal(id, animalDTO);
        return ResponseEntity.status(HttpStatus.OK).body(animalAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animalService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            imageService.delete(fileName);
            return ResponseEntity.ok("File deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting file {}: {}", fileName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete file: " + e.getMessage());
        }
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CaracteristicaDTO>> getAllCaracteristicas() {
        List<CaracteristicaDTO> caracteristicas = animalService.findAllCaracteristicas();
        return ResponseEntity.status(HttpStatus.OK).body(caracteristicas);
    }
}
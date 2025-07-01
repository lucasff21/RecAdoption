package com.recsys.recPet.controller;

import com.recsys.recPet.dto.animal.AnimalUpdateDTO;
import com.recsys.recPet.dto.animal.AnimalCreateDTO;
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
import java.util.Map;

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
    public ResponseEntity<Page<Animal>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) String porte,
            @RequestParam(required = false) List<String> caracteristicas,
            @RequestParam(required = false) String faixaEtaria,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<Animal> animalPage = animalService.findAll(
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
    public ResponseEntity<Animal> findById(@PathVariable Long id) {
        Animal animal = animalService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(animal);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<Animal> save(@Valid @ModelAttribute AnimalCreateDTO animalDTO) throws IOException {
        Animal animal = animalDTO.toEntity();

        if (animalDTO.getImagem() != null && !animalDTO.getImagem().isEmpty()) {
            logger.info("Uploading image...");
            Map<?, ?> image = this.imageService.uploadImage(animalDTO.getImagem(), "pets/adoption");
            animal.setImagePath((String) image.get("secure_url"));
        }

        Animal savedAnimal = animalService.save(animal);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedAnimal);
    }

    @PutMapping(value ="/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Animal> update(
            @PathVariable Long id,
            @Valid @ModelAttribute AnimalUpdateDTO animalDTO
    ) throws IOException {
        try {
            Animal updatedAnimal = animalService.update(animalDTO, id);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAnimal);
        } catch (Exception e) {
            logger.error("Error updating animal with ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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
}
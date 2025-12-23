package com.recsys.recPet.controller;

import com.recsys.recPet.dto.animal.AnimalResponseDTO;
import com.recsys.recPet.dto.admin.animal.AnimalUpdateDTO;
import com.recsys.recPet.dto.admin.animal.AnimalCreateDTO;
import com.recsys.recPet.dto.animal.CaracteristicaDTO;
import com.recsys.recPet.enums.animal.Tipo;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.service.AnimalService;
import com.recsys.recPet.service.CacteristicaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private final AnimalService animalService;
    private final CacteristicaService caracteristicaService;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public AnimalController(AnimalService animalService, CacteristicaService caracteristicaService) {

        this.animalService = animalService;
        this.caracteristicaService = caracteristicaService;
    }

    @GetMapping()
    public ResponseEntity<Page<AnimalResponseDTO>> findAll(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) String porte,
            @RequestParam(value = "caracteristicasIds", required = false) List<Long> caracteristicas,
            @RequestParam(required = false) String faixaEtaria,
            @RequestParam(required = false) Tipo especie,
            @RequestParam(required = false) Boolean castrado,
            @RequestParam(required = false) Boolean vacinado,
            @RequestParam(required = false) String microchip,
            @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<AnimalResponseDTO> animalPage = animalService.findAll(
                nome,
                sexo,
                porte,
                caracteristicas,
                faixaEtaria,
                especie,
                castrado,
                vacinado,
                microchip,
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

    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Animal> save(@Valid @ModelAttribute AnimalCreateDTO animalDTO) throws IOException {
       animalService.criarAnimal(animalDTO);
       return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Animal> atualizarAnimal(@PathVariable Long id, @Valid @ModelAttribute AnimalUpdateDTO animalDTO) throws IOException {
        Animal animalAtualizado = animalService.atualizarAnimal(id, animalDTO);
        return ResponseEntity.status(HttpStatus.OK).body(animalAtualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        animalService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CaracteristicaDTO>> getAllCaracteristicas() {
        List<CaracteristicaDTO> caracteristicas = caracteristicaService.findAllCaracteristicas(null, true);
        return ResponseEntity.status(HttpStatus.OK).body(caracteristicas);
    }
}
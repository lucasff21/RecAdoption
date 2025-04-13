package com.recsys.recPet.controller;

import com.recsys.recPet.model.Cachorro;
import com.recsys.recPet.dto.CachorroDTO;
import com.recsys.recPet.service.CachorroService;
import com.recsys.recPet.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cachorro")
public class CachorroController {

    private final CachorroService cachorroService;
    private final ImageService imageService;

    public CachorroController(CachorroService cachorroService, ImageService imageService) {
        this.cachorroService = cachorroService;
        this.imageService = imageService;
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Cachorro>> findAll() {
        List<Cachorro> cachorroList = cachorroService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(cachorroList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cachorro> findById(@PathVariable Long id) {
        Cachorro cachorro = cachorroService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(cachorro);
    }

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<? extends Object> save(
            @Valid @RequestPart CachorroDTO cachorroDTO,
            @RequestPart("imagem") MultipartFile imagem
    ) throws IOException {
        Cachorro cachorro = cachorroDTO.toEntity();

        if (!imagem.isEmpty()) {
            Map image = this.imageService.uploadImage(imagem, "pets/adoption");
            cachorro.setImagePath((String) image.get("secure_url"));
        }

        cachorroService.save(cachorro);

        return ResponseEntity.status(HttpStatus.CREATED).body(cachorro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cachorro> update(@PathVariable Long id, @RequestBody CachorroDTO cachorroDTO) {
        Cachorro cachorro = new Cachorro();
        BeanUtils.copyProperties(cachorroDTO, cachorro);
        cachorro.setId(id);
        Cachorro cachorroUpdate = cachorroService.update(cachorro);

        return ResponseEntity.status(HttpStatus.OK).body(cachorroUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cachorroService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
       return ResponseEntity.ok("File deleted successfully");
    }
}


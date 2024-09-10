package com.recsys.recPet.controller;

import com.recsys.recPet.animals.Cachorro;
import com.recsys.recPet.dto.CachorroDTO;
import com.recsys.recPet.service.CachorroService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

@RestController
@RequestMapping("/api/cachorro")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CachorroController {

    @Autowired
    private CachorroService cachorroService;

    @GetMapping
    public ResponseEntity<List<Cachorro>> findAll() {
        List<Cachorro> cachorroList = cachorroService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(cachorroList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cachorro> findById(@PathVariable Long id) {
        Cachorro cachorro = cachorroService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(cachorro);
    }

    @PostMapping
    public ResponseEntity<Cachorro> save(@RequestBody CachorroDTO cachorroDTO) {
        Cachorro cachorro = new Cachorro();
        BeanUtils.copyProperties(cachorroDTO, cachorro);
        Cachorro cachorroCreated = cachorroService.save(cachorro);

        return ResponseEntity.status(HttpStatus.CREATED).body(cachorroCreated);
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

    @PostMapping("/uploade-image")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        if (file != null) {
            return new ResponseEntity<>(cachorroService.uploadFile(file), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            String result = cachorroService.deleteFile(fileName);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NoSuchFileException e) {
            return new ResponseEntity<>("File not found: " + fileName, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>("Error deleting file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


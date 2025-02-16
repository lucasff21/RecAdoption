package com.recsys.recPet.controller;

import com.recsys.recPet.animals.Cachorro;
import com.recsys.recPet.dto.CachorroDTO;
import com.recsys.recPet.service.CachorroService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/cachorro")
public class CachorroController {

    @Autowired
    private CachorroService cachorroService;

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

    @PostMapping("/create")
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

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("O arquivo está vazio.", HttpStatus.BAD_REQUEST);
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return new ResponseEntity<>("O arquivo não é uma imagem válida.", HttpStatus.BAD_REQUEST);
            }

            String fileName = cachorroService.uploadFile(file);
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Erro no upload: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao fazer upload: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @GetMapping("/download-image")
    public ResponseEntity<byte[]> getProfileImage(@RequestParam String fileName) {
        try {
            byte[] imageData = cachorroService.downloadFile(fileName);
            String fileType = Files.probeContentType(Paths.get(fileName));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(fileType));

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(imageData);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}


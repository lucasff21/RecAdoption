package com.recsys.recPet.service;

import com.recsys.recPet.model.Cachorro;
import com.recsys.recPet.repository.CachorroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CachorroService {
    private final CachorroRepository cachorroRepository;

    @Value("${file.path}")
    private String filePath;

    public CachorroService(CachorroRepository cachorroRepository) {
        this.cachorroRepository = cachorroRepository;
    }

    public List<Cachorro> findAll() {
        return cachorroRepository.findAll();
    }

    public Cachorro findById(Long id) {
        return cachorroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));
    }

    public Cachorro save(Cachorro cachorro) {
        return cachorroRepository.save(cachorro);
    }

    public Cachorro update(@NotNull Cachorro cachorro) {
        Optional<Cachorro> cachorroOptional = cachorroRepository.findById(cachorro.getId());
        if (cachorroOptional.isPresent()) {
            return cachorroRepository.save(cachorro);
        } else {
            throw new EntityNotFoundException("Animal não encontrado");
        }
    }

    public void delete(Long id) {
        Cachorro cachorro = findById(id);
        cachorroRepository.delete(cachorro);
    }

    public String uploadFile(MultipartFile file) {
        String fileName = System.currentTimeMillis() + "_" + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        if (fileName.contains("..")) {
            throw new IllegalArgumentException("O nome do arquivo contém sequência inválida: " + fileName);
        }

        try {
            // Garantir que o diretório existe
            Path destPath = Paths.get(this.filePath).resolve(fileName);
            Files.createDirectories(destPath.getParent());

            // Salvar o arquivo
            file.transferTo(destPath.toFile());

            return fileName; // Retornar apenas o nome do arquivo
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo: " + e.getMessage(), e);
        }
    }

    public byte[] downloadFile(String fileName) {
        try{
            Path filePath = Paths.get(this.filePath + fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }

    public String deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(this.filePath + fileName);

        if (Files.exists(filePath)) {
            Files.delete(filePath);
            return fileName + " removed successfully.";
        } else {
            throw new NoSuchFileException(fileName);
        }
    }



}

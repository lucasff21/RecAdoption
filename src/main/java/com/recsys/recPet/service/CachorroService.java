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
}

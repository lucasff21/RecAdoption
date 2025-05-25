package com.recsys.recPet.service;

import com.recsys.recPet.dto.cachorro.CachorroUpdateDTO;
import com.recsys.recPet.model.Cachorro;
import com.recsys.recPet.repository.CachorroRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CachorroService {
    private final CachorroRepository cachorroRepository;
    private final ImageService imageService;

    public CachorroService(CachorroRepository cachorroRepository, ImageService imageService) {
        this.cachorroRepository = cachorroRepository;
        this.imageService = imageService;
    }

    public List<Cachorro> findAll() {
        return cachorroRepository.findAll();
    }

    public Cachorro findById(Long id) {
        return cachorroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));
    }

    public void save(Cachorro cachorro) {
        cachorroRepository.save(cachorro);
    }

    public Cachorro update(@NotNull CachorroUpdateDTO cachorroDTO, Long id) throws IOException {
        Cachorro cachorro = cachorroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Animal não encontrado"));

        cachorroDTO.updateEntity(cachorro);

        if (cachorroDTO.getNovaImagem() != null && !cachorroDTO.getNovaImagem().isEmpty()) {
            try {
                Map image = this.imageService.uploadImage(
                        cachorroDTO.getNovaImagem(),
                        "pets/adoption"
                );

                if (cachorro.getImagePath() != null) {
                    this.imageService.delete(cachorro.getImagePath());
                }

                String newImageUrl = (String) image.get("secure_url");

                cachorro.setImagePath(newImageUrl);
            } catch (Exception e) {
                throw new IOException("Falha ao atualizar imagem: " + e.getMessage(), e);
            }
        }

        return cachorroRepository.save(cachorro);
    }

    public void delete(Long id) {
        Cachorro cachorro = findById(id);
        cachorroRepository.delete(cachorro);
    }
}

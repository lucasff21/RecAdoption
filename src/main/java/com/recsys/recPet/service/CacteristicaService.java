package com.recsys.recPet.service;

import com.recsys.recPet.dto.admin.caracteristicas.CaracteristicaFormDTO;
import com.recsys.recPet.dto.animal.CaracteristicaDTO;
import com.recsys.recPet.model.Caracteristica;
import com.recsys.recPet.repository.CaracteristicaRepository;
import com.recsys.recPet.repository.specification.CaracteristicaSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CacteristicaService {
    private final CaracteristicaRepository caracteristicaRepository;

    public CacteristicaService(CaracteristicaRepository caracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Transactional(readOnly = true)
    public List<CaracteristicaDTO> findAllCaracteristicas(String nome, Boolean ativo) {
        Specification<Caracteristica> spec = Specification.where(null);

        if (nome != null && !nome.trim().isEmpty()) {
            spec = spec.and(CaracteristicaSpecification.comNome(nome));
        }

        if (ativo != null) {
            spec = spec.and(CaracteristicaSpecification.comAtivo(ativo));
        }

        List<Caracteristica> caracteristicas = caracteristicaRepository.findAll(spec);

        return caracteristicas.stream()
                .map(CaracteristicaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CaracteristicaDTO create(CaracteristicaFormDTO formDTO) {
        Caracteristica entity = new Caracteristica();
        entity.setNome(formDTO.getNome());
        entity.setDescricao(formDTO.getDescricao());

        Caracteristica salva = caracteristicaRepository.save(entity);
        return CaracteristicaDTO.fromEntity(salva);
    }

    @Transactional
    public CaracteristicaDTO update(Long id, CaracteristicaFormDTO formDTO) {
        Caracteristica entity = caracteristicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Característica não encontrada com ID: " + id));
        entity.setNome(formDTO.getNome());
        entity.setDescricao(formDTO.getDescricao());
        Caracteristica atualizada = caracteristicaRepository.save(entity);
        return CaracteristicaDTO.fromEntity(atualizada);
    }

    @Transactional
    public void desativar(Long id) {
        Optional<Caracteristica> carac = caracteristicaRepository.findById(id);

        if (carac.isEmpty()) {
           return;
        }

        carac.ifPresent(value -> value.setAtivo(false));
    }

    @Transactional
    public void reativar(Long id) {
        Optional<Caracteristica> carac = caracteristicaRepository.findById(id);

        if (carac.isEmpty()) {
            return;
        }

        carac.ifPresent(value -> value.setAtivo(true));
    }
}

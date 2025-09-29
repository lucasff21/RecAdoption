package com.recsys.recPet.controller;

import com.recsys.recPet.dto.adocao.AdocaoDTO;
import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.AdocaoService;
import com.recsys.recPet.service.AnimalService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/adocao")
public class AdocaoController {

    private final AdocaoService adocaoService;
    private final AnimalService animalService;

    public AdocaoController(AdocaoService adocaoService, AnimalService cachorroService) {
        this.adocaoService = adocaoService;
        this.animalService = cachorroService;
    }

    @PostMapping("/create")
    public ResponseEntity<Adocao> save(@RequestBody AdocaoDTO adocaoDTO, @AuthenticationPrincipal User user) throws Exception {
        Adocao adocao = new Adocao();
        adocao.setUser(user);
        adocao.setStatus(AdocaoStatus.PENDENTE);
        Animal animal = animalService.findById(adocaoDTO.animalId());
        if (animal == null) {
            throw new IllegalArgumentException("Animal não encontrado!");
        }
        adocao.setAnimal(animal);
        BeanUtils.copyProperties(adocaoDTO, adocao);
        Adocao adocaoCreated = adocaoService.save(adocao);
        return ResponseEntity.status(HttpStatus.CREATED).body(adocaoCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adocao> update(@PathVariable Long id, @RequestBody AdocaoDTO adocaoDTO) {
        Adocao adocao = new Adocao();
        BeanUtils.copyProperties(adocaoDTO, adocao);
        adocao.setId(id);
        Adocao adocaoUpdate = adocaoService.update(adocao);

        return ResponseEntity.status(HttpStatus.OK).body(adocaoUpdate);
    }

    @GetMapping
    public ResponseEntity<List<AdocaoResponseDTO>> adocaoList(@AuthenticationPrincipal User usuario) {
        List<AdocaoResponseDTO> adocaoList = adocaoService.getAdocoesByUserId(usuario.getId());
        return ResponseEntity.status(HttpStatus.OK).body(adocaoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adocao> findById(@PathVariable Long id) {
        Adocao adocao = adocaoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(adocao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User usuario) {
        adocaoService.deletarAdocaoUusuario(usuario.getId(), id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

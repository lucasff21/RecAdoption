package com.recsys.recPet.controller;

import com.recsys.recPet.dto.AdocaoDTO;
import com.recsys.recPet.model.Adocao;

import com.recsys.recPet.model.Animal;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.AdocaoService;
import com.recsys.recPet.service.CachorroService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/adocao")
public class AdocaoController {

    private final AdocaoService adocaoService;
  
    private final CachorroService cachorroService;

    public AdocaoController(AdocaoService adocaoService, CachorroService cachorroService) {
        this.adocaoService = adocaoService;
        this.cachorroService = cachorroService;
    }

    @PostMapping("/create")
    public ResponseEntity<Adocao> save(@RequestBody AdocaoDTO adocaoDTO, @AuthenticationPrincipal User user) {
        Adocao adocao = new Adocao();
        adocao.setUser(user);
        adocao.setDataAdocao(LocalDate.now());
        Animal animal = cachorroService.findById(adocaoDTO.animalId());
        if (animal == null) {
            throw new IllegalArgumentException("Animal não encontrado!");
        }
        adocao.setAnimal(animal);
        BeanUtils.copyProperties(adocaoDTO, adocao);
        Adocao adocaoCreated = adocaoService.save(adocao);
        return ResponseEntity.status(HttpStatus.CREATED).body(adocaoCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adocao> update(@PathVariable Long id, @RequestBody AdocaoDTO adocaoDTO){
        Adocao adocao = new Adocao();
        BeanUtils.copyProperties(adocaoDTO, adocao);
        adocao.setId(id);
        Adocao adocaoUpdate = adocaoService.update(adocao);

        return ResponseEntity.status(HttpStatus.OK).body(adocaoUpdate);
    }

    @GetMapping
    public ResponseEntity<List<Adocao>> adocaoList(){
        List<Adocao> adocaoList = adocaoService.findAall();
        return ResponseEntity.status(HttpStatus.OK).body(adocaoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adocao> findById(@PathVariable Long id){
        Adocao adoca = adocaoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(adoca);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adocaoService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

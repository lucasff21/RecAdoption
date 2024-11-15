package com.recsys.recPet.controller;

import com.recsys.recPet.dto.AdocaoDTO;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.service.AdocaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adocao")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    public ResponseEntity<Adocao> save(AdocaoDTO adocaoDTO){
        Adocao adocao = new Adocao();
        BeanUtils.copyProperties(adocaoDTO, adocao);
        Adocao adocaoCreated = adocaoService.update(adocao);
        return ResponseEntity.status(HttpStatus.CREATED).body(adocaoCreated);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adocao> update(@PathVariable Long id, @RequestBody AdocaoDTO adocaoDTO){
        Adocao adocao = new Adocao();
        BeanUtils.copyProperties(adocaoDTO, adocao);
        adocao.setId(id);
        Adocao adocaoUpdate = adocaoService.save(adocao);

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

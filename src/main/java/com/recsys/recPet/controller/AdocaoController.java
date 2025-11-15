package com.recsys.recPet.controller;

import com.recsys.recPet.dto.adocao.AdocaoDTO;
import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("isAuthenticated()")
public class AdocaoController {

    private final AdocaoService adocaoService;

    public AdocaoController(AdocaoService adocaoService) {
        this.adocaoService = adocaoService;
    }

    @PostMapping()
    public ResponseEntity<AdocaoResponseDTO> save(@RequestBody AdocaoDTO adocaoDTO, @AuthenticationPrincipal User user) throws Exception {
        Adocao adocaoCriada = adocaoService.criarSolicitacao(adocaoDTO.animalId(), user);
        AdocaoResponseDTO responseDTO = AdocaoResponseDTO.fromEntity(adocaoCriada);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Adocao> update(@PathVariable Long id, @AuthenticationPrincipal User usuario) {

        Adocao adocaoAtualizada = adocaoService.update(id, usuario);
        return ResponseEntity.ok(adocaoAtualizada);
    }

    @GetMapping
    public ResponseEntity<List<AdocaoResponseDTO>> adocaoList(@AuthenticationPrincipal User usuario) {
        List<AdocaoResponseDTO> adocaoList = adocaoService.getAdocoesByUserId(usuario.getId());
        return ResponseEntity.status(HttpStatus.OK).body(adocaoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdocaoResponseDTO> findById(@PathVariable Long id, @AuthenticationPrincipal User usuario) {
        Adocao adocao = adocaoService.findByIdAndUser(id, usuario);
        return ResponseEntity.status(HttpStatus.OK).body(AdocaoResponseDTO.fromEntity(adocao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User usuario) {
        adocaoService.deletarAdocaoUusuario(usuario.getId(), id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

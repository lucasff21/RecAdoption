package com.recsys.recPet.controller;

import com.recsys.recPet.dto.pagina.PaginaResponseDTO;
import com.recsys.recPet.service.PaginaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paginas")
@RequiredArgsConstructor
public class PaginaController {

    private final PaginaService paginaService;

    @GetMapping("/{nome}")
    public ResponseEntity<PaginaResponseDTO> getPaginaPorNome(@PathVariable String nome) {
        PaginaResponseDTO pagina = paginaService.buscarPorNome(nome);
        return ResponseEntity.ok(pagina);
    }
}

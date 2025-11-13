package com.recsys.recPet.controller;

import com.recsys.recPet.dto.questionario.*;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.QuestionarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/questionario")
public class QuestionarioController {

    private final QuestionarioService questionarioService;

    public QuestionarioController(QuestionarioService questionarioService) {
        this.questionarioService = questionarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<QuestionarioResponseDTO> getMeuQuestionario(
            @AuthenticationPrincipal User user
    ) {
        QuestionarioResponseDTO dto = questionarioService.getQuestionarioByUser(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<QuestionarioResponseDTO> createQuestionario(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody QuestionarioCreateDTO dto
    )  {

        QuestionarioResponseDTO questionarioSalvo = questionarioService.create(user, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(questionarioSalvo);
    }

    @PutMapping
    public ResponseEntity<QuestionarioResponseDTO> updateQuestionario(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody QuestionarioCreateDTO dto
    ) {
        QuestionarioResponseDTO questionarioAtualizado = questionarioService.update(user, dto);
        return ResponseEntity.ok(questionarioAtualizado);
    }
}
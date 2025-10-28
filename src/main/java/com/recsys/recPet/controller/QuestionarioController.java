package com.recsys.recPet.controller;

import com.recsys.recPet.dto.QuestionarioDTO;
import com.recsys.recPet.dto.usuario.UsuarioResponseDTO;
import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.QuestionarioService;
import com.recsys.recPet.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionario")
@PreAuthorize("isAuthenticated()")
public class QuestionarioController {

    private final QuestionarioService questionarioService;

    private final UserService userService;

    public QuestionarioController(QuestionarioService questionarioService, UserService userService) {
        this.questionarioService = questionarioService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody QuestionarioDTO questionarioDTO,
                                  @AuthenticationPrincipal User user) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuário autenticado não encontrado");
        }

        Questionario existingQuestionario = questionarioService.findByUser(user.getId());
        if (existingQuestionario != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário já está associado a outro formulário");
        }

        Questionario questionario = new Questionario();
        BeanUtils.copyProperties(questionarioDTO, questionario);

        questionario.setUser(user);

        Questionario questionarioSalvo = questionarioService.save(questionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(questionarioSalvo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Questionario> update(@PathVariable Long id, @RequestBody QuestionarioDTO questionarioDTO) {
        Questionario questionario = questionarioService.findById(id);

        BeanUtils.copyProperties(questionarioDTO, questionario, "user");


        if (questionarioDTO.user() != null && questionarioDTO.user().getId() != null) {
            User user = userService.findById(questionarioDTO.user().getId());
            questionario.setUser(user);
        }

        Questionario questionarioSalvo = questionarioService.update(questionario);

        return ResponseEntity.status(HttpStatus.OK).body(questionarioSalvo);
    }


    @GetMapping()
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<Questionario>> findAll() {
        List<Questionario> questionarioList = questionarioService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(questionarioList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User user) {
        questionarioService.delete(id, user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UsuarioResponseDTO> getProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UsuarioResponseDTO(user));
    }

    @GetMapping("/me")
    public ResponseEntity<Questionario> getMeuQuestionario(@AuthenticationPrincipal User user) {
        Questionario questionario = questionarioService.getQuestionarioByUser(user);
        return ResponseEntity.ok(questionario);
    }
}
package com.recsys.recPet.controller;

import com.recsys.recPet.dto.QuestionarioDTO;
import com.recsys.recPet.dto.UserDTO;
import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.QuestionarioService;
import com.recsys.recPet.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/questionario")
@CrossOrigin(origins = "*", maxAge = 3600)
public class QuestionarioController {

    @Autowired
    private QuestionarioService questionarioService;

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
        user.setQuestionario(questionario);

        // Salva o questionário no banco de dados
        Questionario questionarioSalvo = questionarioService.save(questionario);

        return ResponseEntity.status(HttpStatus.CREATED).body(questionarioSalvo);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Questionario> update(@PathVariable Long id, @RequestBody QuestionarioDTO questionarioDTO){
        Questionario questionario = new Questionario();
        BeanUtils.copyProperties(questionarioDTO, questionario);
       Questionario questionarioSalvo = questionarioService.update(questionario);

        return ResponseEntity.status(HttpStatus.OK).body(questionarioSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Questionario>> findAll(){
        List<Questionario> questionarioList = questionarioService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(questionarioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Questionario> findById(@PathVariable Long id){
        Questionario questionario = questionarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(questionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionarioService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User user) {
        System.out.println("Usuário autenticado: " + user.getEmail());
        return ResponseEntity.ok(user);
    }
}

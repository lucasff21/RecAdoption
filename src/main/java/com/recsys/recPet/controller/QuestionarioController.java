package com.recsys.recPet.controller;

import com.recsys.recPet.dto.QuestionarioDTO;
import com.recsys.recPet.model.Questionario;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.QuestionarioService;
import com.recsys.recPet.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questionario")
public class QuestionarioController {

    @Autowired
    private QuestionarioService questionarioService;

    @Autowired
    private UserService userService;

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
    public ResponseEntity<Questionario> update(@PathVariable Long id, @RequestBody QuestionarioDTO questionarioDTO){

        System.out.println(questionarioDTO);

        Questionario questionario = questionarioService.findById(id);

        BeanUtils.copyProperties(questionarioDTO, questionario, "user");


        if (questionarioDTO.user() != null && questionarioDTO.user().getId() != null) {
            User user = userService.findById(questionarioDTO.user().getId());
            questionario.setUser(user);
        }

        Questionario questionarioSalvo = questionarioService.update(questionario);

        return ResponseEntity.status(HttpStatus.OK).body(questionarioSalvo);
    }


    @GetMapping("/findall")
    public ResponseEntity<List<Questionario>> findAll(){
        List<Questionario> questionarioList = questionarioService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(questionarioList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id){
        Questionario questionario = questionarioService.findById(id);

        if(questionario == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Questionario não encontrado.");
        }

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

    @GetMapping("/email/{email}")
    public ResponseEntity<Questionario> getQuestionarioByEmail(@PathVariable String email) {
        Questionario questionario = questionarioService.getQuestionarioByEmail(email);
        if (questionario != null) {
            return ResponseEntity.ok(questionario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

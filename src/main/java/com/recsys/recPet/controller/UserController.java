package com.recsys.recPet.controller;

import com.recsys.recPet.dto.*;
import com.recsys.recPet.dto.usuario.UserLoginResponseDTO;
import com.recsys.recPet.dto.usuario.UserResponseDTO;
import com.recsys.recPet.dto.usuario.UsuarioAdotanteCreateDTO;
import com.recsys.recPet.dto.usuario.UsuarioLoginDTO;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> authenticateUser(@RequestBody UsuarioLoginDTO loginUserDto) {
        RecoveryJwtTokenDto token = userService.authenticateUser(loginUserDto);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = userService.findByEmail(loginUserDto.getEmail());

        UserResponseDTO userDTO =  new UserResponseDTO(user);

        UserLoginResponseDTO response = new UserLoginResponseDTO(userDTO, token.token());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UsuarioAdotanteCreateDTO createUserDto) {
        userService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        User user  = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(id);
        User userUpdate = userService.update(user);

        return ResponseEntity.status(HttpStatus.OK).body(userUpdate);

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email){
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(
                   new UserResponseDTO(user)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado");
        }
    }
}

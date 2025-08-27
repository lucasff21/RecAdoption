package com.recsys.recPet.controller;

import com.recsys.recPet.dto.*;
import com.recsys.recPet.dto.usuario.*;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService usuarioService;

    public UserController(UserService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> authenticateUser(@RequestBody UsuarioLoginDTO loginUserDto) {
        RecoveryJwtTokenDto token = usuarioService.authenticateUser(loginUserDto);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        User user = usuarioService.findByEmail(loginUserDto.getEmail());

        UsuarioResponseDTO userDTO =  new UsuarioResponseDTO(user);

        UserLoginResponseDTO response = new UserLoginResponseDTO(userDTO, token.token());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UsuarioAdotanteCreateDTO createUserDto) {
        usuarioService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserDTO userDTO){
        User user  = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setId(id);
        User userUpdate = usuarioService.update(user);

        return ResponseEntity.status(HttpStatus.OK).body(userUpdate);

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id){
        User user = usuarioService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/findbyemail/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email){
        try {
            User user = usuarioService.findByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(
                   new UsuarioResponseDTO(user)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> buscarMeuPerfil(Authentication authentication) {
        User usuarioAutenticado = (User) authentication.getPrincipal();
        UsuarioResponseDTO perfil = usuarioService.buscarPerfilPorId(usuarioAutenticado.getId());
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> atualizarMeuPerfil(
            Authentication authentication,
            @Valid @RequestBody UsuarioUpdateDTO usuarioAtualizacaoDTO
    ) {
        User usuarioAutenticado = (User) authentication.getPrincipal();
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarPerfil(usuarioAutenticado.getId(), usuarioAtualizacaoDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}

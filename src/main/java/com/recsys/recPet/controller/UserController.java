package com.recsys.recPet.controller;

import com.recsys.recPet.dto.*;
import com.recsys.recPet.dto.usuario.*;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delete( @AuthenticationPrincipal User usuarioAutenticado) {
        usuarioService.delete(usuarioAutenticado.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> buscarMeuPerfil( @AuthenticationPrincipal User usuarioAutenticado) {
        UsuarioResponseDTO perfil = usuarioService.buscarPerfilPorId(usuarioAutenticado.getId());
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> atualizarMeuPerfil(
            @AuthenticationPrincipal User usuarioAutenticado,
            @Valid @RequestBody UsuarioUpdateDTO usuarioAtualizacaoDTO
    ) {
        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarPerfil(usuarioAutenticado.getId(), usuarioAtualizacaoDTO);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @PostMapping("/password-reset")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> requestPasswordReset(@RequestBody String email) {
        try {
            User user = usuarioService.findByEmail(email);
            usuarioService.setValuesResetPassword(user);
            return ResponseEntity.ok("Se o e-mail estiver correto, você receberá um link.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok("Se o e-mail estiver correto, você receberá um link."); // evita exposição
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }

    @PutMapping("/new-password")
    @PreAuthorize("permitAll()")
    public ResponseEntity<?> resetPassword(@RequestBody String password,
                                           @RequestBody String token) {
        try {
            usuarioService.resetNewPassword(password, token);
            return ResponseEntity.ok("Se o e-mail estiver correto, você receberá um link.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.ok("Se o e-mail estiver correto, você receberá um link.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado");
        }
    }
}

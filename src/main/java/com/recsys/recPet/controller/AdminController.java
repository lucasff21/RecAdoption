package com.recsys.recPet.controller;

import com.recsys.recPet.dto.AdocaoUpdateDTO;
import com.recsys.recPet.dto.UserResponseDTO;
import com.recsys.recPet.dto.admin.AdminAdocaoDTO;
import com.recsys.recPet.dto.admin.CreateUserDTO;
import com.recsys.recPet.dto.admin.UpdateRoleDTO;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.service.AdminService;
import com.recsys.recPet.service.AdocaoService;
import com.recsys.recPet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final AdocaoService adocaoService;

    public AdminController(AdminService adminService, UserService userService, AdocaoService adocaoService) {
        this.adminService = adminService;
        this.userService = userService;
        this.adocaoService = adocaoService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDTO createUserDto) {
        adminService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/role/{id}")
    public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody UpdateRoleDTO updateRoleDto) {
        adminService.updateUser(id, updateRoleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users")
    public Page<UserResponseDTO> findAll(
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "role", required = false) TipoUsuario role,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return userService.findAll(query, role, pageable);
    }

    @GetMapping("/adocoes")
    public ResponseEntity<Page<AdminAdocaoDTO>> getAllAdocoes(
            Pageable pageable
    ) {

        Page<?> resultPage = (Page<?>) adocaoService.findAllAdocoesWithQuestionario(pageable);

        @SuppressWarnings("unchecked")
        Page<AdminAdocaoDTO> adminAdocaoPage = (Page<AdminAdocaoDTO>) resultPage;

        return ResponseEntity.status(HttpStatus.OK).body(adminAdocaoPage);
    }

    @PatchMapping("/adocoes/{id}")
    public ResponseEntity<Adocao> updateStatus(@PathVariable Long id, @RequestBody AdocaoUpdateDTO solicitacaoUpdateDTO) {
        Adocao adocao = adocaoService.findById(id);
        adocao.setStatus(solicitacaoUpdateDTO.getStatus());

        if (solicitacaoUpdateDTO.getStatus() == AdocaoStatus.FINALIZADO) {
            adocao.setConcluidoEm(LocalDateTime.now());
        } else {
            adocao.setConcluidoEm(null);
        }

        if (solicitacaoUpdateDTO.getObservacoes() != null) {
            adocao.setObservacoes(solicitacaoUpdateDTO.getObservacoes());
        }
        Adocao updatedAdocao = adocaoService.update(adocao);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAdocao);
    }
}

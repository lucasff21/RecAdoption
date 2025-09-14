package com.recsys.recPet.controller;

import com.recsys.recPet.dto.admin.animal.AnimalAdminResponseDTO;
import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.dto.adocao.AdocaoUpdateDTO;
import com.recsys.recPet.dto.usuario.UsuarioResponseDTO;
import com.recsys.recPet.dto.admin.CreateUserDTO;
import com.recsys.recPet.dto.admin.UpdateRoleDTO;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.enums.filtro.TipoBusca;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final AdocaoService adocaoService;
    private final AnimalService animalService;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public AdminController(AdminService adminService, UserService userService, AdocaoService adocaoService, AnimalService animalService) {
        this.adminService = adminService;
        this.userService = userService;
        this.adocaoService = adocaoService;
        this.animalService = animalService;
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
    public Page<UsuarioResponseDTO> findAllUsuarios(
            @RequestParam(value = "valor", required = false) String valor,
            @RequestParam(value = "tipoBusca", required = false) TipoBusca tipoBusca,

            @RequestParam(value = "role", required = false) TipoUsuario role,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return userService.findAll(valor, tipoBusca, role, pageable);
    }

    @GetMapping("/adocoes")
    public ResponseEntity<Page<AdocaoResponseDTO>> getAllAdocoes(
            Pageable pageable
    ) {

        Page<Adocao> adocoesPage = adocaoService.findAllAdocoesWithQuestionario(pageable);

        Page<AdocaoResponseDTO> adocaoDtoPage = adocoesPage.map(AdocaoResponseDTO::fromEntity);

        return ResponseEntity.ok(adocaoDtoPage);
    }

    @PatchMapping("/adocoes/{id}")
    public ResponseEntity<Adocao> updateStatus(@PathVariable Long id, @RequestBody AdocaoUpdateDTO solicitacaoUpdateDTO) {
        Adocao adocao = adocaoService.findById(id);
        adocao.setStatus(solicitacaoUpdateDTO.getStatus());

        if (
                !adocao.isConcluida() &&
                (solicitacaoUpdateDTO.getStatus() == AdocaoStatus.FINALIZADO || solicitacaoUpdateDTO.getStatus() == AdocaoStatus.RECUSADO)
        ) {
            adocao.finalizar(solicitacaoUpdateDTO.getStatus());
        }

        if (solicitacaoUpdateDTO.getObservacoes() != null) {
            adocao.setObservacoes(solicitacaoUpdateDTO.getObservacoes());
        }
        Adocao updatedAdocao = adocaoService.update(adocao);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAdocao);
    }

    @GetMapping("/animais")
    public ResponseEntity<Page<AnimalAdminResponseDTO>> findAllAnimais(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) String porte,
            @RequestParam(required = false) List<Long> caracteristicas,
            @RequestParam(required = false) String faixaEtaria,
            @RequestParam(required = false) Boolean disponivelParaAdocao,
            @RequestParam(defaultValue = "0") int page
    ){
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        Page<AnimalAdminResponseDTO> animalPage = animalService.findAllForAdmin(
                nome,
                sexo,
                porte,
                caracteristicas,
                faixaEtaria,
                disponivelParaAdocao,
                pageable
        );
        return ResponseEntity.status(HttpStatus.OK).body(animalPage);
    }
}

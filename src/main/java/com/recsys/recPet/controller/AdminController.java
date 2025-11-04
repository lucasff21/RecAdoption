package com.recsys.recPet.controller;

import com.recsys.recPet.dto.admin.animal.AnimalAdminResponseDTO;
import com.recsys.recPet.dto.adocao.AdocaoResponseDTO;
import com.recsys.recPet.dto.adocao.AdocaoUpdateDTO;
import com.recsys.recPet.dto.pagina.PaginaRequestDTO;
import com.recsys.recPet.dto.pagina.PaginaResponseDTO;
import com.recsys.recPet.dto.usuario.UsuarioResponseDTO;
import com.recsys.recPet.dto.admin.CreateUserDTO;
import com.recsys.recPet.dto.admin.UpdateRoleDTO;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.enums.adocao.AdocaoStatus;
import com.recsys.recPet.enums.filtro.TipoBusca;
import com.recsys.recPet.model.Adocao;
import com.recsys.recPet.model.User;
import com.recsys.recPet.service.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;
    private final AdocaoService adocaoService;
    private final AnimalService animalService;
    private final PaginaService paginaService;

    private static final int DEFAULT_PAGE_SIZE = 20;

    public AdminController(AdminService adminService, UserService userService, AdocaoService adocaoService, AnimalService animalService, PaginaService paginaService) {
        this.adminService = adminService;
        this.userService = userService;
        this.adocaoService = adocaoService;
        this.animalService = animalService;
        this.paginaService = paginaService;
    }

    @PostMapping()
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDTO createUserDto) {
        adminService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
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
            @RequestParam(value = "tipo", required = false) TipoUsuario role,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return userService.findAll(valor, tipoBusca, role, pageable);
    }

    @GetMapping("/adocoes")
    public ResponseEntity<Page<AdocaoResponseDTO>> getAllAdocoes(
            @RequestParam(required = false) AdocaoStatus status,
            @RequestParam(required = false) String termo,
            Pageable pageable
    ) {

        Page<AdocaoResponseDTO> adocaoDtoPage = adocaoService.findAllAdocoes(status, termo, pageable);
        return ResponseEntity.ok(adocaoDtoPage);
    }

    @PatchMapping("/adocoes/{id}")
    public ResponseEntity<Adocao> updateStatus(@PathVariable Long id, @RequestBody AdocaoUpdateDTO solicitacaoUpdateDTO) {
        Adocao adocaoAtualizada = adocaoService.atualizarStatus(id, solicitacaoUpdateDTO);
        return ResponseEntity.ok(adocaoAtualizada);
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

    @PatchMapping("/animais/{id}")
    public ResponseEntity<Void> alterarDisponibilidadeAnimal(@PathVariable Long id, @RequestParam Boolean disponivelParaAdocao) {
        animalService.alterarDisponibilidade(id, disponivelParaAdocao);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/animais/{id}/adocoes")
    public ResponseEntity<Page<AdocaoResponseDTO>> getAdocoesByAnimalId(@PathVariable Long id,  @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<AdocaoResponseDTO> adocoes = adocaoService.findAdocoesByAnimalId(id, pageable);
        return ResponseEntity.ok(adocoes);
    }

    @GetMapping("/paginas")
    public ResponseEntity<Page<PaginaResponseDTO>> listarPaginas(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PaginaResponseDTO> paginas = paginaService.listarTodas(pageable);
        return ResponseEntity.ok(paginas);
    }

    @GetMapping("/paginas/{id}")
    public ResponseEntity<PaginaResponseDTO> getPaginaPorId(@PathVariable Long id) {
        PaginaResponseDTO pagina = paginaService.buscarPorId(id);
        return ResponseEntity.ok(pagina);
    }

    @PostMapping("/paginas")
    public ResponseEntity<PaginaResponseDTO> criarPagina(@Valid @RequestBody PaginaRequestDTO paginaDTO) {
        PaginaResponseDTO novaPagina = paginaService.criarPagina(paginaDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novaPagina.getId())
                .toUri();

        return ResponseEntity.created(location).body(novaPagina);
    }

    @PutMapping("/paginas/{id}")
    public ResponseEntity<PaginaResponseDTO> atualizarPagina(@PathVariable Long id, @Valid @RequestBody PaginaRequestDTO paginaDTO) {
        PaginaResponseDTO paginaAtualizada = paginaService.atualizarPagina(id, paginaDTO);
        return ResponseEntity.ok(paginaAtualizada);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUserById(@PathVariable Long id) {
        User usuario = userService.findById(id);
        return ResponseEntity.ok(new UsuarioResponseDTO(usuario));
    }

    @GetMapping("/users/{id}/adocoes")
    public ResponseEntity<Page<AdocaoResponseDTO>> getAdocoesByUserId(
            @PathVariable Long id,
            @PageableDefault(size = DEFAULT_PAGE_SIZE, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<AdocaoResponseDTO> adocoes = adocaoService.findPageByUserId(id, pageable);
        return ResponseEntity.ok(adocoes);
    }
}

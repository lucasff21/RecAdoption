package com.recsys.recPet.controller;

import com.recsys.recPet.dto.admin.CreateUserDTO;
import com.recsys.recPet.dto.admin.UpdateRoleDTO;
import com.recsys.recPet.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDTO createUserDto) {
        adminService.createUser(createUserDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/change-role/{id}")
    public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody UpdateRoleDTO updateRoleDto) {
        adminService.updateUser(id, updateRoleDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

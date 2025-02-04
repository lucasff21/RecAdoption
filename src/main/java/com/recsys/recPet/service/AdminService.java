package com.recsys.recPet.service;

import com.recsys.recPet.dto.admin.CreateUserDTO;
import com.recsys.recPet.dto.admin.UpdateRoleDTO;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(CreateUserDTO createUserDto) {
        User user = new User();
        user.setNome(createUserDto.getNome());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getSenha()));
        user.setTipoUsuario(createUserDto.getTipo());
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(Long id, UpdateRoleDTO updateRoleDto) {
        User user = userRepository.findById(id).orElseThrow();
        user.setTipoUsuario(updateRoleDto.getTipo());
        userRepository.save(user);
    }
}

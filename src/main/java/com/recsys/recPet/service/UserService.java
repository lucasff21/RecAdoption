package com.recsys.recPet.service;

import com.recsys.recPet.dto.CreateUserDTO;
import com.recsys.recPet.dto.LoginUserDto;
import com.recsys.recPet.dto.RecoveryJwtTokenDto;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeção do PasswordEncoder

    // Método responsável por autenticar um usuário e retornar um token JWT
    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.email(), loginUserDto.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        User userDetails = (User) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(jwtTokenService.generateToken(userDetails));
    }

    // Método responsável por criar um usuário
    public void createUser(CreateUserDTO createUserDto) {
        User newUser = new User();
        newUser.setEmail(createUserDto.email());
        newUser.setPassword(passwordEncoder.encode(createUserDto.password())); // Codificação da senha
        newUser.setTipoUsuario(Set.of(createUserDto.tipoUsuario()));
        userRepository.save(newUser);

    }
}
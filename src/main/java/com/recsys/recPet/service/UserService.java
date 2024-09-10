package com.recsys.recPet.service;

import com.recsys.recPet.dto.CreateUserDTO;
import com.recsys.recPet.dto.LoginUserDto;
import com.recsys.recPet.dto.RecoveryJwtTokenDto;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.security.JwtTokenService;
import com.recsys.recPet.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Set;

public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfiguration securityConfiguration;

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

        // Cria um novo usuário com os dados fornecidos
        User newUser = new User();
        newUser.setEmail(createUserDto.email());
        newUser.setPassword(createUserDto.password())       ;
        newUser.setTipoUsuario(Set.of(createUserDto.tipoUsuario()));

        // Salva o novo usuário no banco de dados
        userRepository.save(newUser);
    }
}

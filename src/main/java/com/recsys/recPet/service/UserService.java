package com.recsys.recPet.service;

import com.recsys.recPet.dto.*;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.Endereco;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.EnderecoRepository;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.security.JwtTokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EnderecoRepository enderecoRepository;

    public UserService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UserRepository userRepository, PasswordEncoder passwordEncoder, EnderecoRepository enderecoRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.enderecoRepository = enderecoRepository;
    }


    public RecoveryJwtTokenDto authenticateUser(LoginUserDto loginUserDto) {
        System.out.println("USUARIO CHEGANDO authenticateUser " + loginUserDto.getEmail());

        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        User userDetails = (User) authentication.getPrincipal();

        String token = jwtTokenService.generateToken(userDetails);

        // Gera um token JWT para o usuário autenticado
        return new RecoveryJwtTokenDto(token);
    }

    // Método responsável por criar um usuário
    public void createUser(CreateAdoptiveUserDTO createUserDto) {
        User newUser = new User();
        newUser.setEmail(createUserDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(createUserDto.getSenha()));
        newUser.setTipoUsuario(TipoUsuario.ADOTANTE);
        newUser.setNome(createUserDto.getNome());
        newUser.setCpf(createUserDto.getCpf());
        newUser.setTelefone(createUserDto.getTelefone());
        newUser.setGenero(createUserDto.getGenero());
        newUser.setDataNascimento(createUserDto.getDataNascimento());
        userRepository.save(newUser);

        Endereco endereco = new Endereco();
        endereco.setUser(newUser);
        endereco.setUf(createUserDto.getUf());
        endereco.setLocalidade(createUserDto.getLocalidade());
        endereco.setBairro(createUserDto.getBairro());
        endereco.setLogradouro(createUserDto.getLogradouro());
        endereco.setCep(createUserDto.getCep());
        endereco.setComplemento(createUserDto.getComplemento());
        enderecoRepository.save(endereco);
    }


    public User update(User userObject){
        User userGet = findById(userObject.getId());

        if(userGet != null){
            userObject.setEmail(userGet.getEmail());
           return userRepository.save(userObject);
        } else {
            throw new EntityNotFoundException("Usuario não encontrado");
        }
    }

    public Page<UserResponseDTO> findAll(String query, TipoUsuario tipoUsuario, Pageable pageable){
        Page<User> users;

        if (query != null && !query.isEmpty()) {
            users = userRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, pageable);
        } else if (tipoUsuario != null) {
            users = userRepository.findByTipoUsuario(tipoUsuario, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(user -> new UserResponseDTO(user));
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public void delete(Long id){
        User user  = findById(id);
        userRepository.delete(user);
    }

    public UserResponseDTO findByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        return new UserResponseDTO(user);
    }
}
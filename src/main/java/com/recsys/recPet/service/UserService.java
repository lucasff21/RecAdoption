package com.recsys.recPet.service;

import com.recsys.recPet.dto.*;
import com.recsys.recPet.dto.usuario.UserResponseDTO;
import com.recsys.recPet.dto.usuario.UsuarioAdotanteCreateDTO;
import com.recsys.recPet.dto.usuario.UsuarioLoginDTO;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.model.Endereco;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.EnderecoRepository;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EnderecoRepository enderecoRepository;

    public UserService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, EnderecoRepository enderecoRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.enderecoRepository = enderecoRepository;
    }


    public RecoveryJwtTokenDto authenticateUser(UsuarioLoginDTO loginUserDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User userDetails = (User) authentication.getPrincipal();

        String token = jwtTokenProvider.generateToken(userDetails);

        return new RecoveryJwtTokenDto(token);
    }


    public void createUser(UsuarioAdotanteCreateDTO createUserDto) {
        User newUser = new User();
        newUser.setEmail(createUserDto.getEmail());
        newUser.setSenha(passwordEncoder.encode(createUserDto.getSenha()));
        newUser.setTipo(TipoUsuario.ADOTANTE);
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
            users = userRepository.findByTipo(tipoUsuario, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users.map(UserResponseDTO::new);
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public void delete(Long id){
        User user  = findById(id);
        userRepository.delete(user);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }
}
package com.recsys.recPet.service;

import com.recsys.recPet.dto.*;
import com.recsys.recPet.dto.usuario.UsuarioResponseDTO;
import com.recsys.recPet.dto.usuario.UsuarioAdotanteCreateDTO;
import com.recsys.recPet.dto.usuario.UsuarioLoginDTO;
import com.recsys.recPet.dto.usuario.UsuarioUpdateDTO;
import com.recsys.recPet.enums.TipoUsuario;
import com.recsys.recPet.enums.filtro.TipoBusca;
import com.recsys.recPet.model.Endereco;
import com.recsys.recPet.model.User;
import com.recsys.recPet.repository.EnderecoRepository;
import com.recsys.recPet.repository.UserRepository;
import com.recsys.recPet.repository.specification.UserSpecification;
import com.recsys.recPet.security.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<UsuarioResponseDTO> findAll(String valor, TipoBusca tipoBusca, TipoUsuario role, Pageable pageable) {
        Specification<User> spec = UserSpecification.filterBy(valor, tipoBusca, role);
        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(UsuarioResponseDTO::new);
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

    @Transactional()
    public UsuarioResponseDTO buscarPerfilPorId(Long usuarioId) {
        return userRepository.findById(usuarioId)
                .map(UsuarioResponseDTO::new)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + usuarioId));
    }
    
    @Transactional
    public UsuarioResponseDTO atualizarPerfil(Long usuarioId, UsuarioUpdateDTO dto) {
        User usuarioParaAtualizar = userRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para atualização"));

        usuarioParaAtualizar.setNome(dto.getNome());
        usuarioParaAtualizar.setTelefone(dto.getTelefone());
        usuarioParaAtualizar.setGenero(dto.getGenero());
        usuarioParaAtualizar.setDataNascimento(dto.getDataNascimento());

        if (dto.getEndereco() != null) {
            Endereco endereco = usuarioParaAtualizar.getEndereco();
            if (endereco == null) {
                endereco = new Endereco();
                usuarioParaAtualizar.setEndereco(endereco);
                endereco.setUser(usuarioParaAtualizar);
            }
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setLogradouro(dto.getEndereco().getLogradouro());
            endereco.setComplemento(dto.getEndereco().getComplemento());
            endereco.setBairro(dto.getEndereco().getBairro());
            endereco.setLocalidade(dto.getEndereco().getCidade());
            endereco.setUf(dto.getEndereco().getEstado());
        }

        User usuarioSalvo = userRepository.save(usuarioParaAtualizar);

        return new UsuarioResponseDTO(usuarioSalvo);
    }
}